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

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(project(":nosql"))
  implementation(project(":analytics-server-core"))
}
