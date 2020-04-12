@file:Suppress("UnstableApiUsage")

plugins {
    `gradle-enterprise`
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(System.getenv("CI")?.toBoolean() == true)
    }
}

rootProject.name = "maadb-project"

include(
    ":core",
    ":sql",
//    ":nosql",
    ":sql-analytics-server",
    ":sql-analytics-core",
    ":docker"
)
