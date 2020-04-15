import com.github.lamba92.gradle.utils.kotlinx
import com.github.lamba92.gradle.utils.lamba
import com.github.lamba92.gradle.utils.serialization

plugins {
    id("maadb-project-plugin")
    kotlin("plugin.serialization")
}

kotlin.sourceSets["jvmMain"].dependencies {
    val krlVersion: String by project
    val openNlpVersion: String by project
    val emojiJavaVersion: String by project
    val emoji4jJavaVersion: String by project
    val kotlinxSerializationVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib-jdk8"))

    api(lamba("kresourceloader", krlVersion)) {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    }

    api("org.apache.opennlp:opennlp-tools:$openNlpVersion")
    api("com.vdurmont:emoji-java:$emojiJavaVersion")
    api("com.kcthota:emoji4j:$emoji4jJavaVersion")
    api(serialization("runtime", kotlinxSerializationVersion))
    api(kotlinx("coroutines-core", coroutinesVersion))

}
