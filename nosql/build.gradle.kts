plugins {
    `maadb-library-plugin`
    kotlin("plugin.serialization")
}

kotlin.sourceSets["jvmMain"].dependencies {

    val kMongoVersion: String by project
    val logbackVersion: String by project

    api(project(":core"))
    api("org.litote.kmongo:kmongo-coroutine-serialization:$kMongoVersion")
    api("ch.qos.logback:logback-classic:$logbackVersion")

}
