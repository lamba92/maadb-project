package edu.unito.maadb.mongosupervisor

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.utils.getTweetsElaborationChunkedFlow
import edu.unito.maadb.nosql.dsl.createCollection
import edu.unito.maadb.sql.tables.*
import edu.unito.maadb.sql.utils.populateResources
import edu.unito.maadb.sql.utils.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.system.exitProcess
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
suspend fun main() {

    val configs = getEnvSplitOrThrow("MONGO_CONFIGS")
    val shards = getEnvSplitOrThrow("MONGO_SHARDS")
    val configsReplicaName = getEnvOrThrow("MONGO_CONFIGS_RPL_NAME")
    val shardsReplicaName = getEnvOrThrow("MONGO_SHARDS_RPL_NAME")

    val MONGO_DB_NAME: String by System.getenv()

    val RELATIONAL_DB_URL: String by System.getenv()
    val RELATIONAL_DB_USER: String by System.getenv()
    val RELATIONAL_DB_PASSWORD: String by System.getenv()

    initializeReplicaSet(configs, configsReplicaName, true)
    initializeReplicaSet(shards, shardsReplicaName)

    val process = initializeShardSet(configsReplicaName, configs, shardsReplicaName, shards)

    waitUntilMongoIsUp(port = 27017)

    mongoEval(command = "sh.enableSharding('$MONGO_DB_NAME')")
    mongoEval(command = "sh.shardCollection('$MONGO_DB_NAME.tweets', { originalTweet: 'hashed' })")

    val tweetsCollection = KMongo.createClient()
        .getDatabase(MONGO_DB_NAME)
        .coroutine
        .createCollection<ElaboratedTweet>("tweets")

    val postgresDB = Database.connect(
        RELATIONAL_DB_URL,
        user = RELATIONAL_DB_USER,
        password = RELATIONAL_DB_PASSWORD,
        driver = org.postgresql.Driver::class.qualifiedName!!
    )

    newSuspendedTransaction(db = postgresDB) {
        SchemaUtils.createMissingTablesAndColumns(
            TweetsTable,
            TweetEmojisTable,
            TweetEmoticonsTable,
            TweetHashtagsTable,
            SimpleResourcesTable,
            WithSentimentResourcesTable,
            SimplePairResourcesTable,
            WithSentimentPairResourcesTable
        )
    }

    try {
        populateResources(postgresDB)
    } catch (e: Throwable) {
        println("Resources already in DB")
    }

    getTweetsElaborationChunkedFlow(2000)
        .collect {
            coroutineScope {
                launch {
                    try {
                        tweetsCollection.insertMany(it)
                    } catch (e: Throwable) {
                        println("Tweets with ID from ${it.first()._id} to ${it.last()._id} already in MongoDB")
                    }
                }
                launch {
                    try {
                        newSuspendedTransaction(db = postgresDB) {
                            it.map { it.toEntity() }
                        }
                    } catch (e: Throwable) {
                        println("Tweets with ID from ${it.first()._id} to ${it.last()._id} already in Relationa DB")
                    }
                }
            }
        }

    exitProcess(withContext(Dispatchers.IO) {
        process.waitFor()
    })

}


