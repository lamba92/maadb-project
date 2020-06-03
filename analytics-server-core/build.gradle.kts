@file:Suppress("UNUSED_VARIABLE")

import com.github.lamba92.gradle.utils.ktor

plugins {
  `maadb-library-plugin`
  kotlin("plugin.serialization")
}

// workaround for https://youtrack.jetbrains.com/issue/KT-38165
val copySourcesWorkaround by tasks.creating(Sync::class) {
  from("$rootDir/core/src/jvmMain/resources")
  into("$buildDir/workarounds/resources")
}

kotlin {
  jvm {
    compilations["main"].compileKotlinTask.dependsOn(copySourcesWorkaround)
  }
  sourceSets {
    val ktorVersion: String by project
    val logbackVersion: String by project
    jvmMain {
      resources.srcDir(copySourcesWorkaround.destinationDir)
      dependencies {
        implementation(kotlin("stdlib-jdk8"))

        api(project(":analytics-core"))
        api(project(":core"))

        api(ktor("server-tomcat", ktorVersion))
        api(ktor("serialization", ktorVersion))
        api(ktor("locations", ktorVersion))

        api("ch.qos.logback:logback-classic:$logbackVersion")
      }
    }
  }
}
