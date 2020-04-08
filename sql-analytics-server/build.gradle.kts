@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("jvm")
    application
}

application {
    mainClassName = "edu.unito.maadb.sql.analytics.server.MainKt"
}

dependencies {

    val ktorVersion: String by project
    val mySqlDriversVersion: String by project
    val sqliteDriversVersion: String by project
    val postgresDriversVersion: String by project

    api(project(":sql-analytics-core"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor", "ktor-server-jetty", ktorVersion)
    implementation("io.ktor", "ktor-serialization", ktorVersion)
    implementation("io.ktor", "ktor-locations", ktorVersion)
    implementation("mysql", "mysql-connector-java", mySqlDriversVersion)
    implementation("org.xerial", "sqlite-jdbc", sqliteDriversVersion)
    implementation("org.postgresql", "postgresql", postgresDriversVersion)
}

task<Copy>("copyDistToDocker") {
    dependsOn(tasks.distTar)
    from(tasks.distTar.get())
    into("$rootDir/docker/sql-analytics-server/build")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    distTar {
        archiveFileName.set(project.name + ".tar")
    }
}
