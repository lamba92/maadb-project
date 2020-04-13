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
    api("com.github.lamba92:kresourceloader:$krlVersion") {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
    }
    api("org.apache.opennlp:opennlp-tools:$openNlpVersion")
    api("com.vdurmont:emoji-java:$emojiJavaVersion")
    api("com.kcthota:emoji4j:$emoji4jJavaVersion")
    api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinxSerializationVersion")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

}
