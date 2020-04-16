import com.github.lamba92.gradle.utils.kotlinJvm
import it.lamba.utils.getResource
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.bundling.Tar
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import java.io.ByteArrayOutputStream

class MaadbApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {

        apply<KotlinPlatformJvmPlugin>()
        apply<ApplicationPlugin>()

        kotlinJvm.target.compilations.all {
            kotlinOptions.jvmTarget = "1.8"
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

        if (shouldSetupDocker)
            tasks {

                val distTar by getting(Tar::class)

                val dockerBuildFolder = file("$buildDir/dockerBuild").absolutePath

                val copyDistTar by creating(Copy::class) {
                    dependsOn(distTar)
                    group = "docker"
                    from(distTar.archiveFile.get())
                    into(dockerBuildFolder)
                }

                val copyDockerfile by creating(Copy::class) {
                    group = "docker"
                    from(getResource("Dockerfile"))
                    into(dockerBuildFolder)
                }

                val buildMultiArchImages by creating(Exec::class) {
                    dependsOn(copyDistTar, copyDockerfile)
                    group = "docker"
                    commandLine(
                        "docker",
                        "buildx",
                        "build",
                        "--build-arg APP_NAME=${project.name}",
                        "--build-arg TAR_NAME=${distTar.archiveFile.get().asFile.name}",
                        "-t",
                        "lamba92/${rootProject.name}-${project.name}",
                        "--platform=linux/amd64,linux/arm64,linux/arm",
                        dockerBuildFolder
                    )
                }

                "build" {
                    dependsOn(buildMultiArchImages)
                }

                create<Exec>("publishMultiArchImages") {
                    dependsOn(copyDistTar, copyDockerfile)
                    group = "docker"
                    commandLine(
                        "docker",
                        "buildx",
                        "build",
                        "--build-arg APP_NAME=${project.name}",
                        "--build-arg TAR_NAME=${distTar.archiveFile.get().asFile.name}",
                        "-t",
                        "lamba92/${rootProject.name}-${project.name}:${project.version}",
                        "--platform=linux/amd64,linux/arm64,linux/arm",
                        dockerBuildFolder,
                        "--push"
                    )
                }

            }
    }
}
