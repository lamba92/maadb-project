pluginManagement {
    repositories {
        jcenter()
        mavenCentral()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://plugins.gradle.org/m2/")
    }
    resolutionStrategy {
        val kotlinVersion: String by settings
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.plugin.serialization", "org.jetbrains.kotlin.multiplatform" ->
                    useVersion(kotlinVersion)
            }
        }
    }
}

rootProject.name = "MAADB-project"
include(
    ":core",
    ":sql",
    ":sql-analytics-server",
    ":sql-analytics-core"
)
