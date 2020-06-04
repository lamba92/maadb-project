plugins {
  `maadb-application-plugin`
}

// workaround for https://youtrack.jetbrains.com/issue/KT-38165
val copySourcesWorkaround by tasks.creating(Copy::class) {
  from("$rootDir/core/src/jvmMain/resources")
  into("$projectDir/src/main/resources")
}

kotlin.target.compilations["main"].compileKotlinTask.dependsOn(copySourcesWorkaround)

dependencies {

  val postgresDriversVersion: String by project

  implementation(kotlin("stdlib-jdk8"))

  implementation(project(":analytics-server-core"))
  implementation(project(":sql"))

  implementation("org.postgresql", "postgresql", postgresDriversVersion)
}
