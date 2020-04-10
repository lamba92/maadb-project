plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
}

allprojects {
    group = "com.github.lamba92"
    version = "1.0.8"
}

subprojects {
    repositories {
        maven("https://jitpack.io")
        mavenCentral()
        jcenter()
    }
}
