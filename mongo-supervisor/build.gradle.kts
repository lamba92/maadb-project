import com.github.lamba92.gradle.utils.kotlinx
import com.github.lamba92.gradle.utils.serialization
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

    val kotlinxSerializationVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation(serialization("runtime", kotlinxSerializationVersion))
    implementation(kotlinx("coroutines-core", coroutinesVersion))

}

tasks {

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
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
