plugins {
  `maadb-application-plugin`
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(project(":nosql"))
  implementation(project(":analytics-server-core"))
}
