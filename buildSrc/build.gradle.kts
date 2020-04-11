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
    maven("https://plugins.gradle.org/m2/")
}

dependencies {

    val kotlinVersion: String by project
    val sshGradlePluginVersion: String by project
    val bintrayPluginVersion: String by project
    val guavaVersion: String by project

    api(kotlin("stdlib-jdk8", kotlinVersion))
    api(kotlin("reflect", kotlinVersion))
    api("org.jetbrains.kotlin", "kotlin-gradle-plugin", kotlinVersion)
    api("com.jfrog.bintray.gradle", "gradle-bintray-plugin", bintrayPluginVersion)
    api("org.hidetake", "gradle-ssh-plugin", sshGradlePluginVersion)
    api("org.jetbrains.kotlin", "kotlin-serialization", kotlinVersion)
    api("com.google.guava", "guava", guavaVersion)

}
