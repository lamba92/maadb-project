package edu.unito.maadb.mongosupervisor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
suspend fun main() {

    val configs = getEnvSplitOrThrow("CONFIGS")
    val shards = getEnvSplitOrThrow("SHARDS")
    val configsReplicaName = getEnvOrThrow("CONFIGS_RPL_NAME")
    val shardsReplicaName = getEnvOrThrow("SHARDS_RPL_NAME")

    initializeReplicaSet(configs, configsReplicaName, true)
    initializeReplicaSet(shards, shardsReplicaName)

    val process = initializeShardSet(configsReplicaName, configs, shardsReplicaName, shards)

    waitUntilMongoIsUp(port = 27017)

    mongoEval(command = "sh.enableSharding('maadb')")
    mongoEval(command = "sh.shardCollection('maadb.tweets', { originalTweet: 'hashed' })")

    exitProcess(withContext(Dispatchers.IO) {
        process.waitFor()
    })

}


