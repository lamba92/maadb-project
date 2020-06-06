package edu.unito.maadb.analytics.nosql

import edu.unito.maadb.analytics.core.DatasourceElaborator
import edu.unito.maadb.analytics.core.PagedData
import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.sql.analytics.core.StatisticsResult
import edu.unito.maadb.sql.analytics.core.TweetsStatisticsResult
import it.lamba.utils.getResource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.math.ceil
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
abstract class AbstractMongoDBDatasource : DatasourceElaborator {

  @Serializable
  data class StatsReduceResult(
      @SerialName("_id") val word: String,
      @SerialName("value") val occurrences: Double
  )

  protected abstract val tweetsCollection: CoroutineCollection<ElaboratedTweet>

  override suspend fun statsTweets(sentiment: SpecificSentiment): TweetsStatisticsResult {
      val (data, duration) = measureTimedValue {
          val wordsCounted = tweetsCollection.mapReduce<StatsReduceResult>(
              getResource("statsTweetMap.js")
                  .readText()
                  .replace("%%%SENTIMENT%%%", sentiment.toString()),
              getResource("statsReduce.js").readText()
          )
              .toList()
              .associate { it.word to it.occurrences.toInt() }

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
          tweetsCollection.mapReduce<StatsReduceResult>(
              getResource("statsHashtagsMap.js")
                  .readText()
                  .replace("%%%SENTIMENT%%%", sentiment.toString()),
              getResource("statsReduce.js").readText()
          )
              .toList()
              .associate { it.word to it.occurrences.toInt() }
      }
    return StatisticsResult(sentiment, hashtagsCounted, duration.toLongMilliseconds())
  }

  override suspend fun statsEmoticons(sentiment: SpecificSentiment): StatisticsResult {
      val (emoticonsCounted, duration) = measureTimedValue {
          tweetsCollection.mapReduce<StatsReduceResult>(
              getResource("statsEmoticonsMap.js")
                  .readText()
                  .replace("%%%SENTIMENT%%%", sentiment.toString()),
              getResource("statsReduce.js").readText()
          )
              .toList()
              .associate { it.word to it.occurrences.toInt() }
      }
    return StatisticsResult(sentiment, emoticonsCounted, duration.toLongMilliseconds())
  }

  override suspend fun statsEmojis(sentiment: SpecificSentiment): StatisticsResult {
      val (emojisCounted, duration) = measureTimedValue {
          tweetsCollection.mapReduce<StatsReduceResult>(
              getResource("statsEmojisMap.js")
                  .readText()
                  .replace("%%%SENTIMENT%%%", sentiment.toString()),
              getResource("statsReduce.js").readText()
          )
              .toList()
              .associate { it.word to it.occurrences.toInt() }
      }
    return StatisticsResult(sentiment, emojisCounted, duration.toLongMilliseconds())
  }

  override suspend fun tweets(
      sentiment: SpecificSentiment,
      page: Int,
      pageSize: Int
  ): PagedData<List<ElaboratedTweet>> = coroutineScope {
    val totalPages = async {
      ceil(tweetsCollection.countDocuments(ElaboratedTweet::sentiment eq sentiment) / pageSize.toFloat())
          .toInt()
    }
    val data = async {
      tweetsCollection.find(ElaboratedTweet::sentiment eq sentiment)
          .skip(page * pageSize)
          .limit(pageSize)
          .toList()
    }
    PagedData(data.await(), page, pageSize, totalPages.await())
  }
}

@OptIn(ExperimentalTime::class)
object Datasource : AbstractMongoDBDatasource() {

  override val tweetsCollection = KMongo.createClient(System.getenv("MONGO_URL") ?: "mongodb://192.168.1.158:27017")
      .getDatabase(System.getenv("MONGO_DB_NAME") ?: "maadb")
      .coroutine
      .getCollection<ElaboratedTweet>("tweets")

}
