plugins {
    `maadb-application-plugin`
}

// workaround for https://youtrack.jetbrains.com/issue/KT-38165
val copySourcesWorkaround by tasks.creating(Copy::class) {
    from("$rootDir/core/src/jvmMain/resources")
    into("$projectDir/src/main/resources")
}

kotlin.target.compilations["main"].compileKotlinTask.dependsOn(copySourcesWorkaround)

application {
    mainClassName = "edu.unito.maadb.datafeeder.sql.MainKt"
}

dependencies {
    implementation(project(":sql"))
}
