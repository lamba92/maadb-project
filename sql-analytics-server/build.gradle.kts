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

dependencies {

    val ktorVersion: String by project
    val logbackVersion: String by project

//    implementation(project(":sql-analytics-core"))

    implementation(kotlin("stdlib-jdk8"))

    implementation("io.ktor", "ktor-server-tomcat", ktorVersion)
    implementation("io.ktor", "ktor-serialization", ktorVersion)
    implementation("io.ktor", "ktor-locations", ktorVersion)
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("com.github.lamba92:maadb-project-sql-analytics-core-jvm:1.0.8")

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    distTar {
        archiveFileName.set(project.name + ".tar")
    }
}
