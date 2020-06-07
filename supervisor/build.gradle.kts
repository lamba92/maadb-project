import com.github.lamba92.gradle.utils.kotlinx
import com.github.lamba92.gradle.utils.serialization

plugins {
    `maadb-application-plugin`
    kotlin("plugin.serialization")
}

// workaround for https://youtrack.jetbrains.com/issue/KT-38165
val copySourcesWorkaround by tasks.creating(Copy::class) {
    from("$rootDir/core/src/jvmMain/resources")
    into("$projectDir/src/main/resources")
}

kotlin.target.compilations["main"].compileKotlinTask.dependsOn(copySourcesWorkaround)

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
