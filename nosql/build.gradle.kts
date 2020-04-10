plugins {
    id("maadb-project-plugin")
}

kotlin.sourceSets["jvmMain"].dependencies {

    val kMongoVersion: String by project

    api(project(":core"))
    api("org.litote.kmongo:kmongo:$kMongoVersion")
    api("org.litote.kmongo:kmongo-coroutine:$kMongoVersion")

}
