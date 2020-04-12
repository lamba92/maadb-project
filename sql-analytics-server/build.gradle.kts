@file:Suppress("UNUSED_VARIABLE")

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

    implementation("io.ktor", "ktor-server-tomcat", ktorVersion)
    implementation("io.ktor", "ktor-serialization", ktorVersion)
    implementation("io.ktor", "ktor-locations", ktorVersion)
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    distTar {
        archiveFileName.set(project.name + ".tar")
    }
}
