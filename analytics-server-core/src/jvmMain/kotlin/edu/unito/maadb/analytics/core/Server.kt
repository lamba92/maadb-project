package edu.unito.maadb.analytics.core

import edu.unito.maadb.core.ElaboratedTweet
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CachingHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.CachingOptions
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.response.respondOutputStream
import io.ktor.response.respondText
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@OptIn(KtorExperimentalLocationsAPI::class, ExperimentalTime::class)
fun getServer(datasource: DatasourceElaborator) = embeddedServer(Tomcat) {

    install(ContentNegotiation) {
        json()
    }

    install(Locations)

    install(CachingHeaders) {
        options { outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Image.PNG -> CachingOptions(CacheControl.MaxAge(10.minutes.inSeconds.toInt()))
                else -> null
            }
        }
    }

    routing {
        route("wordClouds") {
            route("tweets") {
                get<SentimentLocation> { (sentiment) ->
                    val wc = defaultWordCloud(datasource.statsTweets(sentiment).wordsWithOccurrences)
                    call.respondOutputStream(ContentType.Image.PNG, HttpStatusCode.OK) {
                        withContext(Dispatchers.IO) {
                            wc.writeToStreamAsPNG(this@respondOutputStream)
                        }
                    }
                }
            }
            route("hashtags") {
                get<SentimentLocation> { (sentiment) ->
                    val wc = defaultWordCloud(datasource.statsHashtags(sentiment).dataWithOccurrences)
                    call.respondOutputStream(ContentType.Image.PNG, HttpStatusCode.OK) {
                        withContext(Dispatchers.IO) {
                            wc.writeToStreamAsPNG(this@respondOutputStream)
                        }
                    }
                }

            }
            route("emoticons") {
                get<SentimentLocation> { (sentiment) ->
                    val wc = defaultWordCloud(datasource.statsEmoticons(sentiment).dataWithOccurrences)
                    call.respondOutputStream(ContentType.Image.PNG, HttpStatusCode.OK) {
                        withContext(Dispatchers.IO) {
                            wc.writeToStreamAsPNG(this@respondOutputStream)
                        }
                    }
                }

            }
            route("emojis") {
                get<SentimentLocation> { (sentiment) ->
                    val wc = defaultWordCloud(datasource.statsEmojis(sentiment).dataWithOccurrences)
                    call.respondOutputStream(ContentType.Image.PNG, HttpStatusCode.OK) {
                        withContext(Dispatchers.IO) {
                            wc.writeToStreamAsPNG(this@respondOutputStream)
                        }
                    }
                }
            }
        }
        route("statistics") {
            route("tweets") {
                get<SentimentLocation> { (sentiment) ->
                    call.respond(datasource.statsTweets(sentiment))
                }
            }
            route("hashtags") {
                get<SentimentLocation> { (sentiment) ->
                    call.respond(datasource.statsHashtags(sentiment))
                }
            }
            route("emoticons") {
                get<SentimentLocation> { (sentiment) ->
                    call.respond(datasource.statsEmoticons(sentiment))
                }
            }
            route("emojis") {
                get<SentimentLocation> { (sentiment) ->
                    call.respond(datasource.statsEmojis(sentiment))
                }
            }
        }
        route("data") {
            route("tweets") {
                get<SentimentPagedLocation> { (emotion, page, pageSize) ->
                    val deserializer = Json(JsonConfiguration.Stable)
                    call.respondText(
                        deserializer.stringify(
                            PagedData.serializer(ElaboratedTweet.serializer().list),
                            datasource.tweets(emotion, page, pageSize)
                        ),
                        ContentType.Application.Json
                    )
                }
            }
        }
    }
}

