plugins {
    `kotlin-dsl`
}

gradlePlugin.plugins.create("maadb-library-plugin") {
    id = "maadb-library-plugin"
    implementationClass = "MaadbLibraryPlugin"
}
gradlePlugin.plugins.create("maadb-application-plugin") {
    id = "maadb-application-plugin"
    implementationClass = "MaadbApplicationPlugin"
}

repositories {
    jcenter()
    mavenCentral()
    google()
    maven("https://jitpack.io")
    maven("https://plugins.gradle.org/m2/")
    maven("https://dl.bintray.com/lamba92/com.github.lamba92")
}

dependencies {

    val kotlinVersion: String by project
    val lambaGradleUtilsVersion: String by project
    val resLoaderVersion: String by project
    val guavaVersion: String by project

    api(kotlin("stdlib-jdk8", kotlinVersion))
    api(kotlin("reflect", kotlinVersion))
    api("com.github.lamba92", "lamba-gradle-utils", lambaGradleUtilsVersion)
    api("com.github.lamba92", "kresourceloader", resLoaderVersion)
    api("com.google.guava", "guava", guavaVersion)
}
