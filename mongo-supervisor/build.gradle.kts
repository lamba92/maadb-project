import org.gradle.internal.os.OperatingSystem
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

application {
    mainClassName = "edu.unito.maadb.mongosupervisor.MainKt"
}

dependencies {

    val kMongoVersion: String by project
    val kBsonVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    api("org.litote.kmongo:kmongo:$kMongoVersion")
    api("org.litote.kmongo:kmongo-coroutine:$kMongoVersion")
    api("com.github.jershell:kbson:$kBsonVersion")

}

tasks {

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    distTar {
        archiveFileName.set(project.name + ".tar")
    }

    // Check OS first, if using Win10Home this exec can take a lot of time
    // due to Docker Toolbox under VirtualBox cold start
    val shouldSetupDocker = if (OperatingSystem.current().isLinux)
        exec {
            commandLine("docker")
            standardOutput = ByteArrayOutputStream()
            errorOutput = ByteArrayOutputStream()
        }.exitValue == 0
    else
        false

    if (shouldSetupDocker) {

        val dockerBuildFolder = file("$buildDir/dockerBuild").absolutePath

        val copyDistTar by registering(Sync::class) {
            group = "docker"
            from(distTar.get().archiveFile.get())
            into(dockerBuildFolder)
        }

        val copyDockerfile by registering(Sync::class) {
            group = "docker"
            from("$projectDir/Dockerfile")
            into(dockerBuildFolder)
        }

        val buildMultiArchImages by registering(Exec::class) {
            dependsOn(copyDistTar, copyDockerfile)
            group = "docker"
            commandLine(
                "docker",
                "buildx",
                "build",
                "-t",
                "lamba92/${rootProject.name}-${project.name}",
                "--platform=linux/amd64,linux/arm64,linux/arm",
                dockerBuildFolder
            )
        }

        build {
            dependsOn(buildMultiArchImages)
        }

        val publishMultiArchImages by registering(Exec::class) {
            dependsOn(copyDistTar, copyDockerfile)
            group = "docker"
            commandLine(
                "docker",
                "buildx",
                "build",
                "-t",
                "lamba92/${rootProject.name}-${project.name}",
                "--platform=linux/amd64,linux/arm64,linux/arm",
                dockerBuildFolder,
                "--push"
            )
        }

    }

}