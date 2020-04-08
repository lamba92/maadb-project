package edu.unito.maadb.sql.analytics.server

import io.ktor.application.Application
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty

@KtorExperimentalLocationsAPI
fun main() {
    embeddedServer(
        Jetty,
        module = Application::sqlAnalyticServer
    ).start(true)
}
