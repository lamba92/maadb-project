package edu.unito.maadb.analytics.sql

import edu.unito.maadb.analytics.core.DatasourceElaborator
import edu.unito.maadb.analytics.core.PagedData
import edu.unito.maadb.analytics.core.merge
import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.sql.analytics.core.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import edu.unito.maadb.sql.daos.TweetEmojiEntity
import edu.unito.maadb.sql.daos.TweetEmoticonEntity
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.daos.TweetHashtagEntity
import edu.unito.maadb.sql.tables.TweetEmojisTable
import edu.unito.maadb.sql.tables.TweetEmoticonsTable
import edu.unito.maadb.sql.tables.TweetHashtagsTable
import edu.unito.maadb.sql.tables.TweetsTable
import edu.unito.maadb.sql.utils.toModel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.sql.Connection
import kotlin.math.ceil
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
object Datasource : DatasourceElaborator {

  val db by lazy {
    Database.connect(
        url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://192.168.1.158:5432/",
        user = System.getenv("DATABASE_USER") ?: "postgres",
        password = System.getenv("DATABASE_PASSWORD") ?: "postgres",
        driver = org.postgresql.Driver::class.qualifiedName!!
    ).also {
      TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }
  }

  override suspend fun statsTweets(sentiment: SpecificSentiment): TweetsStatisticsResult {
    val (data, duration) = measureTimedValue {
      val wordsCounted = newSuspendedTransaction(db = db) {
        TweetEntity.find { TweetsTable.sentiment eq sentiment.toString() }
            .map { it.stemmedTweetWithOccurrences }
            .merge()
      }

      val resources = Resources.LexicalData.Sentiments.Specific.getValue(sentiment)
          .EVERY_RESOURCE
          .filter { "_" !in it }

      val newWords = wordsCounted.keys.filter { it !in resources }
      wordsCounted to newWords
    }

    return TweetsStatisticsResult(sentiment, data.first, data.second, duration.toLongMilliseconds())
  }

  override suspend fun statsHashtags(sentiment: SpecificSentiment): StatisticsResult {
    val (hashtagsCounted, duration) = measureTimedValue {
      newSuspendedTransaction(db = db) {
        TweetHashtagEntity.find { TweetHashtagsTable.sentiment eq sentiment.toString() }
            .map { it.hashtag to it.count }
            .merge()
      }
    }
    return StatisticsResult(sentiment, hashtagsCounted, duration.toLongMilliseconds())
  }

  override suspend fun statsEmoticons(sentiment: SpecificSentiment): StatisticsResult {
    val (emoticonsCounted, duration) = measureTimedValue {
      newSuspendedTransaction(db = db) {
        TweetEmoticonEntity.find { TweetEmoticonsTable.sentiment eq sentiment.toString() }
            .map { it.emoticon to it.count }
            .merge()
      }
    }
    return StatisticsResult(sentiment, emoticonsCounted, duration.toLongMilliseconds())
  }

  override suspend fun statsEmojis(sentiment: SpecificSentiment): StatisticsResult {
    val (emojisCounted, duration) = measureTimedValue {
      newSuspendedTransaction(db = db) {
        TweetEmojiEntity.find { TweetEmojisTable.sentiment eq sentiment.toString() }
            .map { it.emoji to it.count }
            .merge()
      }
    }
    return StatisticsResult(sentiment, emojisCounted, duration.toLongMilliseconds())
  }

  override suspend fun tweets(sentiment: SpecificSentiment, page: Int, pageSize: Int): PagedData<List<ElaboratedTweet>> {
    val (totalPages, data) = newSuspendedTransaction(db = db) {
      TweetEntity.find { TweetsTable.sentiment eq sentiment.toString() }.let {
        ceil(it.count() / pageSize.toFloat()).toInt() to it.limit(
                pageSize,
                page * pageSize.toLong()
            )
            .asFlow()
            .map { it.toModel() }
            .toList()
      }
    }

    return PagedData(data, page, pageSize, totalPages)
  }
}
