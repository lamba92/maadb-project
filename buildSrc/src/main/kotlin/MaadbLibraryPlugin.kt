import com.github.lamba92.gradle.utils.asLambda
import com.github.lamba92.gradle.utils.kotlinMultiplatform
import com.github.lamba92.gradle.utils.prepareForPublication
import com.github.lamba92.gradle.utils.publishing
import com.jfrog.bintray.gradle.BintrayPlugin
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

open class MaadbLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {

        apply<KotlinMultiplatformPluginWrapper>()
        apply<MavenPublishPlugin>()
        apply<BintrayPlugin>()

        val mavenAction = Action<MavenPublication> {
            if (!artifactId.startsWith(rootProject.name))
                artifactId = "${rootProject.name}-$artifactId"
        }

        kotlinMultiplatform {
            jvm {
                compilations.all {
                    kotlinOptions {
                        jvmTarget = "1.8"

                        @Suppress("SuspiciousCollectionReassignment")
                        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
                    }
                }
            }
        }

        publishing.publications.withType(mavenAction.asLambda())
        prepareForPublication()
    }
}
