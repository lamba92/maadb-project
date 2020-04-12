import com.google.common.base.CaseFormat
import org.gradle.internal.os.OperatingSystem
import org.hidetake.groovy.ssh.core.Remote
import org.hidetake.groovy.ssh.core.RunHandler
import org.hidetake.groovy.ssh.core.Service
import org.hidetake.groovy.ssh.session.SessionHandler
import java.io.ByteArrayOutputStream

plugins {
    id("org.hidetake.ssh")
}

// Check OS first, if using Win10Home this exec can take a lot of time
// due to Docker Toolbox under VirtualBox cold start
val isDockerInstalled = if (OperatingSystem.current().isLinux)
    exec {
        commandLine("docker")
        standardOutput = ByteArrayOutputStream()
    }.exitValue == 0
else
    false

if (isDockerInstalled && System.getenv("CI")?.toBoolean() == true) {

    val distTar by evaluationDependsOn(":sql-analytics-server").tasks.named<Tar>("distTar")

    val copySqlAnalyticsServerDistTar by tasks.registering(Sync::class) {
        from(distTar.archiveFile)
        into("$projectDir/src/sql-analytics-server")
    }

    val projects = file("$projectDir/src").listFiles()!!
        .filter { it.isDirectory }
        .map { it!! to (CaseFormat.LOWER_HYPHEN.converterTo(CaseFormat.UPPER_CAMEL).convert(it.name) ?: it.name) }

    val buildTasks = projects.map { (folder, camelName) ->
        task<Exec>("build${camelName}Image") {
            if (folder.name == "sql-server-analytics")
                dependsOn(copySqlAnalyticsServerDistTar)
            group = "docker"
            commandLine(
                "docker",
                "buildx",
                "build",
                "-t",
                "lamba92/jupyter-kotlin:$version",
                "--platform=linux/amd64,linux/arm64,linux/arm",
                folder.absolutePath
            )

            logging.captureStandardOutput(LogLevel.INFO)

        }
    }

    task("build") {
        group = "build"
        dependsOn(*buildTasks.toTypedArray())
    }

    val publishTasks = projects.map { (folder, camelName) ->
        task<Exec>("publish${camelName}Image") {
            group = "docker"
            if (folder.name == "sql-server-analytics")
                dependsOn(copySqlAnalyticsServerDistTar)
            commandLine(
                "docker",
                "buildx",
                "build",
                "-t",
                "lamba92/jupyter-kotlin:$version",
                "--platform=linux/amd64,linux/arm64,linux/arm",
                folder.absolutePath,
                "--push"
            )
            logging.captureStandardOutput(LogLevel.INFO)
        }
    }

    task("publish") {
        group = "publishing"
        dependsOn(*publishTasks.toTypedArray())
    }

}

val rpi4 by remotes.creating {
    val rpi4Address: String by project
    user = "ubuntu"
    host = rpi4Address
    identity = file("${System.getProperty("user.home")}/.ssh/id_rsa")
}

val rpi2 by remotes.creating {
    val rpi2Address: String by project
    user = "ubuntu"
    host = rpi2Address
    identity = file("${System.getProperty("user.home")}/.ssh/id_rsa")
}

val copyDockerData by tasks.creating {
    group = "docker"
    val distTar by evaluationDependsOn(":sql-analytics-server").tasks.named<Tar>("distTar")
    dependsOn(distTar)
    doLast {
        ssh.runSessions {
            session(rpi4, rpi2) {
                execute("mkdir -p docker")
                execute("rm -r -f ~/docker/*")
                file("$projectDir/src").listFiles()!!.forEach {
                    put(it, "./docker")
                    if (it.name == "sql-analytics-server")
                        put(distTar.archiveFile.get().asFile, "./docker/sql-analytics-server")
                }
            }
        }
    }
}

task("dockerComposeUp") {
    group = "docker"
    dependsOn(copyDockerData)
    doLast {
        ssh.runSessions {
            session(rpi4) {
                execute("cd ./docker")
                val out = execute("docker-compose up -d")
                println(out)
            }
        }
    }
}

task("dockerComposeStop") {
    group = "docker"
    dependsOn(copyDockerData)
    doLast {
        ssh.runSessions {
            session(rpi4) {
                execute("cd ./docker")
                val out = execute("docker-compose stop")
                println(out)
            }
        }
    }
}

fun Service.runSessions(action: RunHandler.() -> Unit) {
    run(delegateClosureOf(action))
}

fun RunHandler.session(vararg remotes: Remote, action: SessionHandler.() -> Unit) =
    remotes.forEach { remote ->
        session(remote, delegateClosureOf(action))
    }

fun SessionHandler.put(src: String, dst: String) {
    put(hashMapOf("from" to src, "into" to dst))
}

fun SessionHandler.put(src: File, dst: String) {
    put(hashMapOf("from" to src, "into" to dst))
}
