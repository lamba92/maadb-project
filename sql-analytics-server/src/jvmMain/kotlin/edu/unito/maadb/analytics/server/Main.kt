package edu.unito.maadb.analytics.server

import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty

@KtorExperimentalLocationsAPI
fun main() {
    embeddedServer(
        Jetty,
        port = System.getenv("DB_PORT")?.toInt() ?: 8080,
        module = Application::sqlAnalyticServer
    ).start(true)
}
