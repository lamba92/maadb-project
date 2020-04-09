@file:Suppress("UNUSED_VARIABLE")

plugins {
    id("maadb-project-plugin")
    kotlin("plugin.serialization")
}

kotlin.sourceSets["jvmMain"].dependencies {

    val postgresDriversVersion: String by project
    val mySqlDriversVersion: String by project
    val sqliteDriversVersion: String by project

    implementation(kotlin("stdlib-jdk8"))

    api(project(":sql"))
    api("org.postgresql:postgresql:$postgresDriversVersion")
    api("mysql:mysql-connector-java:$mySqlDriversVersion")
    api("org.xerial:sqlite-jdbc:$sqliteDriversVersion")

}
