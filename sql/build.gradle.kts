@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("maadb-project-plugin")
    kotlin("plugin.serialization")
}

kotlin.sourceSets["jvmMain"].dependencies {
    val exposedVersion: String by project
    val coroutinesVersion: String by project
    val logbackVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    api(project(":core")) {
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
    }
    api("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    api("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")
}

