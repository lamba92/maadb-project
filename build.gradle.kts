import com.github.lamba92.gradle.utils.TRAVIS_TAG

plugins {
    kotlin("multiplatform") apply false
    kotlin("plugin.serialization") apply false
}

allprojects {
    group = "com.github.lamba92"
    version = TRAVIS_TAG ?: "1.0.8"
}

subprojects {
    repositories {
        maven("https://jitpack.io")
        mavenCentral()
        jcenter()
    }
}
