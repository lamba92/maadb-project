plugins {
    id("maadb-library-plugin")
    kotlin("plugin.serialization")
}

kotlin.sourceSets["jvmMain"].dependencies {

    val kMongoVersion: String by project
    val kBsonVersion: String by project

    api(project(":core"))
//    api("org.litote.kmongo:kmongo:$kMongoVersion")
    api("org.litote.kmongo:kmongo-coroutine-serialization:$kMongoVersion")
    api("com.github.jershell:kbson:$kBsonVersion")

}
