package edu.unito.maadb.sql.analytics.server

import edu.unito.maadb.core.Resources
import edu.unito.maadb.sql.analytics.core.HashtagsStatisticsResult
import edu.unito.maadb.sql.analytics.core.TweetsStatisticsResult
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.daos.TweetHashtagEntity
import edu.unito.maadb.sql.tables.TweetHashtagsTable
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

                get<EmotionLocation> { param ->
                    val wordsCounted = newSuspendedTransaction(db = db) {
                        TweetEntity.find { TweetsTable.sentiment eq param.emotion.toString() }
                            .map { it.stemmedTweetWithOccurrences }
                            .merge()
                    }

                    val resources = Resources.LexicalData.Emotions.Specific
                        .getValue(param.emotion)
                        .filter { "_" !in it }

                    val newWords = wordsCounted.keys.filter { it !in resources }

                    call.respond(
                        TweetsStatisticsResult(
                            param.emotion,
                            wordsCounted,
                            newWords
                        )
                    )

                }
            }

            route("hashtags") {
                get<EmotionLocation> { param ->
                    val hashtagsCounted = newSuspendedTransaction {
                        TweetHashtagEntity.find { TweetHashtagsTable.sentiment eq param.emotion.toString() }
                            .map { it.hashtag to it.count }
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
