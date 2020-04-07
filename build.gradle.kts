plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
}

allprojects {
    group = "com.github.lamba92"
    version = "1.0-SNAPSHOT"
}

subprojects {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://jitpack.io")
        mavenCentral()
        jcenter()
    }
}
