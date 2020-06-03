plugins {
  `maadb-application-plugin`
}

dependencies {

  val postgresDriversVersion: String by project

  implementation(kotlin("stdlib-jdk8"))

  implementation(project(":analytics-server-core"))
  implementation(project(":sql"))

  implementation("org.postgresql", "postgresql", postgresDriversVersion)
}
