@file:Suppress("UNUSED_VARIABLE")

plugins {
    kotlin("multiplatform")
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                api(project(":sql-analytics-core"))
            }
        }

        val jvmMain by getting {
            dependencies {
                val ktorVersion: String by project
                val mySqlDriversVersion: String by project
                val sqliteDriversVersion: String by project
                implementation(kotlin("stdlib-jdk8"))
                implementation("io.ktor:ktor-server-jetty:$ktorVersion")
                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("io.ktor:ktor-locations:$ktorVersion")
                implementation("mysql:mysql-connector-java:$mySqlDriversVersion")
                implementation("org.xerial:sqlite-jdbc:$sqliteDriversVersion")

            }
        }
    }
}
