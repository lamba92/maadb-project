@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("maadb-project-plugin")
    kotlin("plugin.serialization")
}

kotlin.sourceSets {

    val commonMain by getting {
        dependencies {
            api(project(":sql"))
        }
    }

    val jvmMain by getting {
        dependencies {
            val postgresDriversVersion: String by project
            implementation(kotlin("stdlib-jdk8"))
            implementation("org.postgresql:postgresql:$postgresDriversVersion")
        }
    }

}
