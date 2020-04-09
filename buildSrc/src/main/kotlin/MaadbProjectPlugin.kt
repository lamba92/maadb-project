import com.jfrog.bintray.gradle.BintrayPlugin
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

open class MaadbProjectPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {

        apply<KotlinMultiplatformPluginWrapper>()
        apply<MavenPublishPlugin>()
        apply<BintrayPlugin>()

        val mavenAction = Action<MavenPublication> {
            if (!artifactId.startsWith(rootProject.name))
                artifactId = "${rootProject.name}-$artifactId"
        }

        kotlin {
            jvm {
                compilations.all {
                    kotlinOptions {
                        jvmTarget = "1.8"
                        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
                    }
                }
            }
        }

        publishing.publications.withType(mavenAction.asLambda())

        bintray {
            user = searchPropertyOrNull("bintrayUsername")
            key = searchPropertyOrNull("bintrayApiKey")
            pkg {
                version {
                    name = project.version as String
                }
                repo = group as String
                name = rootProject.name
                setLicenses("Apache-2.0")
                vcsUrl = "https://github.com/lamba92/maadb-project"
                issueTrackerUrl = "https://github.com/lamba92/maadb-project/issues"
            }
            publish = true
            setPublications(publishing.publications.names)
        }

        tasks.withType<BintrayUploadTask> {
            doFirst {
                publishing.publications.withType<MavenPublication> {
                    buildDir.resolve("publications/$name/module.json").also {
                        if (it.exists())
                            artifact(object : FileBasedMavenArtifact(it) {
                                override fun getDefaultExtension() = "module"
                            })
                    }
                }
            }
        }
    }

}
