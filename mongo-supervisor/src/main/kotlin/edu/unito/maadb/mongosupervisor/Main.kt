package edu.unito.maadb.mongosupervisor


suspend fun main() {

    val configs = getEnvSplitOrThrow("CONFIGS")
    val shards = getEnvSplitOrThrow("SHARDS")
    val configsReplicaName = getEnvOrThrow("CONFIGS_RPL_NAME")
    val shardsReplicaName = getEnvOrThrow("SHARDS_RPL_NAME")

    initializeReplicaSet(configs, configsReplicaName, true)
    initializeReplicaSet(shards, shardsReplicaName)

    initializeShardSet(shards, shardsReplicaName)

}


