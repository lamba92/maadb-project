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
    ":nosql",
    ":mongo-supervisor",
    ":analytics-server-core",
    ":sql-analytics-server",
    ":nosql-analytics-server",
    ":analytics-core",
    "sql-data-feeder",
    "nosql-data-feeder"
)
