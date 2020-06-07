import com.github.lamba92.gradle.utils.kotlinx
import com.github.lamba92.gradle.utils.serialization

plugins {
    `maadb-application-plugin`
    kotlin("plugin.serialization")
}

application {
    mainClassName = "edu.unito.maadb.mongosupervisor.MainKt"
}

dependencies {

    val kotlinxSerializationVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation(serialization("runtime", kotlinxSerializationVersion))
    implementation(kotlinx("coroutines-core", coroutinesVersion))
    implementation(project(":sql"))
    implementation(project(":nosql"))
}
