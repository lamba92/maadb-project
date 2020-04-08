import org.hidetake.groovy.ssh.core.Remote
import org.hidetake.groovy.ssh.core.RunHandler
import org.hidetake.groovy.ssh.core.Service
import org.hidetake.groovy.ssh.session.SessionHandler

plugins {
    id("org.hidetake.ssh")
}

val dockerRemote by remotes.creating {
    val remoteDockerMachineAddress: String by project
    user = "ubuntu"
    host = remoteDockerMachineAddress
    identity = file("${System.getProperty("user.home")}/.ssh/id_rsa")
}

val copyDockerData by tasks.creating {
    group = "docker"
    val distTar by evaluationDependsOn(":sql-analytics-server").tasks.named<Tar>("distTar")
    dependsOn(distTar)
    doLast {
        ssh.runSessions {
            session(dockerRemote) {
                execute("rm -r -f ./docker")
                execute("mkdir -p docker")
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
            session(dockerRemote) {
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
            session(dockerRemote) {
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

fun RunHandler.session(remote: Remote, action: SessionHandler.() -> Unit) {
    session(remote, delegateClosureOf(action))
}

fun SessionHandler.put(src: String, dst: String) {
    put(hashMapOf("from" to src, "into" to dst))
}

fun SessionHandler.put(src: File, dst: String) {
    put(hashMapOf("from" to src, "into" to dst))
}
