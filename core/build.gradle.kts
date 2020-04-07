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

        val jvmMain by getting {
            dependencies {
                val krlVersion: String by project
                val openNlpVersion: String by project
                implementation(kotlin("stdlib-jdk8"))
                api("com.github.lamba92:kresourceloader:$krlVersion") {
                    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
                }
                api("org.apache.opennlp:opennlp-tools:$openNlpVersion")
            }
        }

    }
}
