package edu.unito.maadb.sql.analytics.server

import edu.unito.maadb.core.Resources
import edu.unito.maadb.sql.analytics.core.StatisticsResult
import edu.unito.maadb.sql.analytics.core.TweetsStatisticsResult
import edu.unito.maadb.sql.daos.TweetEmojiEntity
import edu.unito.maadb.sql.daos.TweetEmoticonEntity
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.daos.TweetHashtagEntity
import edu.unito.maadb.sql.tables.TweetEmojisTable
import edu.unito.maadb.sql.tables.TweetEmoticonsTable
import edu.unito.maadb.sql.tables.TweetHashtagsTable
import edu.unito.maadb.sql.tables.TweetsTable
import edu.unito.maadb.sql.utils.toModel
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.Connection
import kotlin.math.ceil

@KtorExperimentalLocationsAPI
fun Application.sqlAnalyticServer() {

    val db by lazy {
        Database.connect(
            url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://192.168.1.158:5432/maadb",
            user = System.getenv("DATABASE_USER") ?: "postgres",
            password = System.getenv("DATABASE_PASSWORD") ?: "postgres"
        ).also {
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        }
    }

    install(ContentNegotiation) {
        json()
    }

    install(Locations)

    routing {
        route("statistics") {
            route("tweets") {
                get<SentimentLocation> { (sentiment) ->
                    val wordsCounted = newSuspendedTransaction(db = db) {
                        TweetEntity.find { TweetsTable.sentiment eq sentiment.toString() }
                            .map { it.stemmedTweetWithOccurrences }
                            .merge()
                    }

                    val resources = Resources.LexicalData.Sentiments.Specific
                        .getValue(sentiment)
                        .EVERY_RESOURCE
                        .filter { "_" !in it }

                    val newWords = wordsCounted.keys.filter { it !in resources }

                    call.respond(TweetsStatisticsResult(sentiment, wordsCounted, newWords))
                }
            }
            route("hashtags") {
                get<SentimentLocation> { (sentiment) ->
                    val hashtagsCounted = newSuspendedTransaction(db = db) {
                        TweetHashtagEntity.find { TweetHashtagsTable.sentiment eq sentiment.toString() }
                            .map { it.hashtag to it.count }
                            .merge()
                    }
                    call.respond(StatisticsResult(sentiment, hashtagsCounted))
                }
            }
            route("emoticons") {
                get<SentimentLocation> { (sentiment) ->
                    val emoticonsCounted = newSuspendedTransaction(db = db) {
                        TweetEmoticonEntity.find { TweetEmoticonsTable.sentiment eq sentiment.toString() }
                            .map { it.emoticon to it.count }
                            .merge()
                    }
                    call.respond(StatisticsResult(sentiment, emoticonsCounted))
                }
            }
            route("emojis") {
                get<SentimentLocation> { (sentiment) ->
                    val emojisCounted = newSuspendedTransaction(db = db) {
                        TweetEmojiEntity.find { TweetEmojisTable.sentiment eq sentiment.toString() }
                            .map { it.emoji to it.count }
                            .merge()
                    }
                    call.respond(StatisticsResult(sentiment, emojisCounted))
                }
            }
        }
        route("data") {
            route("tweets") {
                get<SentimentPagedLocation> { (emotion, page, pageSize) ->
                    val (totalPages, data) = newSuspendedTransaction(db = db) {
                        TweetEntity.find { TweetsTable.sentiment eq emotion.toString() }.let {
                            ceil(it.count() / pageSize.toFloat()).toInt() to it.limit(
                                    pageSize,
                                    page * pageSize.toLong()
                                )
                                .asFlow()
                                .map { it.toModel() }
                                .toList()
                        }
                    }
//                    val deserializer = Json(JsonConfiguration.Stable)
                    call.respond(PagedData(data, page, pageSize, totalPages))
//                    call.respondText(
//                        deserializer.stringify(
//                            PagedData.serializer(ElaboratedTweet.serializer().list),
//                            PagedData(data, page, pageSize, totalPages)
//                        ),
//                        ContentType.Application.Json
//                    )
                }
            }
        }
    }
}
