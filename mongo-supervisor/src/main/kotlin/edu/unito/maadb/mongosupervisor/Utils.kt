package edu.unito.maadb.mongosupervisor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

fun getEnvSplitOrThrow(name: String) =
        System.getenv(name)?.split(",")
                ?.map { it.split(":").let { Remote(it[0], it[1].toInt()) } }
                ?: throw IllegalArgumentException("$name missing from environment")

data class Remote(val host: String, val port: Int) {
    override fun toString() = "$host:$port"
}

fun getEnvOrThrow(name: String) =
        System.getenv(name) ?: throw IllegalArgumentException("$name missing from environment")

inline fun ReplicaConfigurationDocument.members(action: ReplicaMembersBuilder.() -> Unit) =
        ReplicaMembersBuilder().apply(action).also { members = it.build() }

suspend fun checkIfMongoIsUp(host: String = "localhost", port: Int = 27019) =
        mongoEval(host, port, "db.stats()") == 0

@OptIn(ExperimentalTime::class)
suspend fun waitUntilMongoIsUp(host: String = "localhost", port: Int = 27019, delay: Duration = 10.seconds) {
    while (!checkIfMongoIsUp(host, port)) {
        println("mongodb://$host:$port not yet up, waiting 10 seconds...")
        delay(delay)
    }
    println("Host $host:$port is up")
}

suspend fun mongoEval(host: String = "localhost", port: Int = 27017, builder: StringBuilder.() -> Unit) =
        mongoEval(host, port, buildString(builder))

suspend fun mongoEval(host: String = "localhost", port: Int = 27017, command: String) = withContext(Dispatchers.IO) {
    val commands = arrayOf("mongo", "--host", host, "--port", port.toString(), "--eval", command)
    println("MONGO EVAL $host:$port | ${commands.joinToString(" ")}")
    with(ProcessBuilder("mongo", "--host", host, "--port", port.toString(), "--eval", command)) {
        val output = createTempFile()
        redirectOutput(output)
        val exitValue = start().waitFor()
        println("Process output:")
        output.readLines()
                .map { "\t $it" }
                .forEach { println(it) }
        exitValue
    }
}

suspend inline fun initializeReplicaSet(host: String, port: Int, action: ReplicaConfigurationDocument.() -> Unit) {
    val serializer = Json(JsonConfiguration.Stable.copy(prettyPrint = true))
    val doc = ReplicaConfigurationDocument().apply(action)
            .let { serializer.stringify(ReplicaConfigurationDocument.serializer(), it) }
    println("initializing replica set: \n${doc}")
    mongoEval(host, port, "rs.initiate($doc)")
}

suspend fun initializeReplicaSet(
        remotes: List<Remote>,
        replicaSetName: String,
        enableConfigurationServer: Boolean = false
) {
    val (mainHost, mainPort) = remotes.first()
    initializeReplicaSet(mainHost, mainPort) init@{
        id = replicaSetName
        this@init.enableConfigurationServer = enableConfigurationServer
        members {
            remotes.forEachIndexed { index, (memberHost, memberPort) ->
                waitUntilMongoIsUp(memberHost, memberPort)
                add {
                    id = index
                    host = "$memberHost:$memberPort"
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
suspend fun initializeShardSet(
    configsReplicaName: String,
    configs: List<Remote>,
    replicaSetName: String,
    shards: List<Remote>
): Process {
    val mongosProcess = withContext(Dispatchers.IO) {
        ProcessBuilder(
            "mongos",
            "--configdb",
            "$configsReplicaName/${configs.joinToString(",")}",
            "--bind_ip_all"
        )
            .inheritIO()
            .start()
    }

    waitUntilMongoIsUp(port = 27017)

    while (mongoEval("localhost", 27017) {
            append("sh.addShard(\"")
            append(replicaSetName)
            append("/")
            append(shards.joinToString(",") { "${it.host}:${it.port}" })
            append("\")")
        } != 0) {
        println("Failed to initialize sharded cluster. Retrying...")
        delay(5.seconds)
    }

    return mongosProcess
}

