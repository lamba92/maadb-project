@file:Suppress("UNUSED_VARIABLE")

import com.github.lamba92.gradle.utils.ktor

plugins {
    `maadb-library-plugin`
    kotlin("plugin.serialization")
}



kotlin {

    sourceSets {

        val ktorVersion: String by project
        val logbackVersion: String by project
        val kumoVersion: String by project

        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))

                api(project(":analytics-core"))
                api(project(":core"))

                api(ktor("server-tomcat", ktorVersion))
                api(ktor("serialization", ktorVersion))
                api(ktor("locations", ktorVersion))

                api("com.kennycason:kumo-core:$kumoVersion")
                api("ch.qos.logback:logback-classic:$logbackVersion")
            }
        }
    }
}
