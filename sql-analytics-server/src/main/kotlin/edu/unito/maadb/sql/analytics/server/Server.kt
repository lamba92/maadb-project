package edu.unito.maadb.sql.analytics.server

import edu.unito.maadb.core.Resources
import edu.unito.maadb.sql.analytics.core.HashtagsStatisticsResult
import edu.unito.maadb.sql.analytics.core.TweetsStatisticsResult
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.tables.TweetsTable
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
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.Connection

@KtorExperimentalLocationsAPI
fun Application.sqlAnalyticServer() {

    val db by lazy {
        Database.connect(
            url = System.getenv("DATABASE_URL") ?: "jdbc:sqlite://db.sqlite",
            user = System.getenv("DATABASE_USER") ?: "",
            password = System.getenv("DATABASE_PASSWORD") ?: ""
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

                get<EmotionLocation> { param ->
                    val wordsCounted = newSuspendedTransaction(db = db) {
                        TweetEntity.find { TweetsTable.sentiment eq param.emotion.toString() }
                            .map { it.stemmedTweetWithOccurrences }
                            .merge()
                    }
                    val newWords =
                        wordsCounted.keys.filter { it !in Resources.LexicalData.Emotions.Specific.getValue(param.emotion) }
                    call.respond(
                        TweetsStatisticsResult(
                            wordsCounted,
                            newWords
                        )
                    )
                }

            }

            route("hashtags") {

                get<EmotionLocation> { param ->
                    val hashtagsCounted = newSuspendedTransaction {
                        TweetEntity.find { TweetsTable.sentiment eq param.emotion.toString() }
                            .map { it.hashtags.map { it.hashtag to it.count }.toMap() }
                            .merge()
                    }
                    call.respond(
                        HashtagsStatisticsResult(
                            param.emotion,
                            hashtagsCounted
                        )
                    )
                }
            }

        }

    }
}
