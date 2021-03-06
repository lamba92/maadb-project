package edu.unito.maadb.sql.utils

import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.core.utils.TweetsElaborationTools
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.tables.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@OptIn(ExperimentalStdlibApi::class, FlowPreview::class)
suspend fun populateDatabase(
        database: Database,
        tools: TweetsElaborationTools = TweetsElaborationTools.Defaults
): Map<SpecificSentiment, List<TweetEntity>> {

    newSuspendedTransaction(db = database) {
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

    populateResources(database)
    return populateTweets(tools, database)

}

fun populateDatabaseBlocking(
        database: Database,
        tools: TweetsElaborationTools = TweetsElaborationTools.Defaults
) = runBlocking {
    populateDatabase(
            database,
            tools
    )
}


