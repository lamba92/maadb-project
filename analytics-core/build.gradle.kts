@file:Suppress("UNUSED_VARIABLE")

plugins {
    `maadb-library-plugin`
    kotlin("plugin.serialization")
}

kotlin.sourceSets.jvmMain {
    dependencies {
        api(project(":core"))
    }
}
