@file:Suppress("UNUSED_VARIABLE")

import com.github.lamba92.gradle.utils.ktor
import org.gradle.internal.os.OperatingSystem
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

application {
    mainClassName = "edu.unito.maadb.sql.analytics.server.MainKt"
}

repositories {
    maven("https://dl.bintray.com/lamba92/com.github.lamba92")
}

// workaround for https://youtrack.jetbrains.com/issue/KT-38165
val copySourcesWorkaround by tasks.registering(Sync::class) {
    from("$rootDir/core/src/jvmMain/resources")
    into("$buildDir/workarounds/resources")
}
kotlin.target.compilations["main"].compileKotlinTask.dependsOn(copySourcesWorkaround.get())
sourceSets["main"].resources.srcDir(copySourcesWorkaround.get().destinationDir)

dependencies {

    val ktorVersion: String by project
    val logbackVersion: String by project

    implementation(project(":sql-analytics-core"))

    implementation(kotlin("stdlib-jdk8"))

    implementation(ktor("server-tomcat", ktorVersion))
    implementation(ktor("serialization", ktorVersion))
    implementation(ktor("locations", ktorVersion))

    implementation("ch.qos.logback:logback-classic:$logbackVersion")

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

        val copyDistTar by registering(Copy::class) {
            dependsOn(distTar)
            group = "docker"
            from(distTar.get().archiveFile.get())
            into(dockerBuildFolder)
        }

        val copyDockerfile by registering(Copy::class) {
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
