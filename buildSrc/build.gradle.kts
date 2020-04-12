plugins {
    `kotlin-dsl`
}

gradlePlugin.plugins.create("firebase-multiplatform-gradle-plugin") {
    id = "maadb-project-plugin"
    implementationClass = "MaadbProjectPlugin"
}

repositories {
    jcenter()
    mavenCentral()
    google()
    maven("https://plugins.gradle.org/m2/")
    maven("https://dl.bintray.com/lamba92/com.github.lamba92")
}

dependencies {

    val kotlinVersion: String by project
    val lambaGradleUtilsVersion: String by project
    val guavaVersion: String by project

    api(kotlin("stdlib-jdk8", kotlinVersion))
    api(kotlin("reflect", kotlinVersion))
    api("com.github.lamba92", "lamba-gradle-utils", lambaGradleUtilsVersion)
    api("com.google.guava", "guava", guavaVersion)
}
