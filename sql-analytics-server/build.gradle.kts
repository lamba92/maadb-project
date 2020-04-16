@file:Suppress("UNUSED_VARIABLE")

import com.github.lamba92.gradle.utils.ktor

plugins {
    id("maadb-application-plugin")
    kotlin("plugin.serialization")
}

application {
    mainClassName = "edu.unito.maadb.sql.analytics.server.MainKt"
}

repositories {
    maven("https://dl.bintray.com/lamba92/com.github.lamba92")
}

// workaround for https://youtrack.jetbrains.com/issue/KT-38165
val copySourcesWorkaround by tasks.creating(Sync::class) {
    from("$rootDir/core/src/jvmMain/resources")
    into("$buildDir/workarounds/resources")
}

kotlin.target.compilations["main"].compileKotlinTask.dependsOn(copySourcesWorkaround)
sourceSets["main"].resources.srcDir(copySourcesWorkaround.destinationDir)

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
