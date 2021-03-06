package edu.unito.maadb.nosql.utils

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import edu.unito.maadb.core.utils.TweetsElaborationTools
import edu.unito.maadb.core.utils.getTweetsElaborationChunkedFlow
import edu.unito.maadb.nosql.dsl.createCollection
import edu.unito.maadb.nosql.dsl.storageEngineOptions
import edu.unito.maadb.nosql.model.ListResource
import edu.unito.maadb.nosql.model.ListResourceWithSentiment
import edu.unito.maadb.nosql.model.MapResource
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

suspend fun populateTweets(
    database: CoroutineDatabase,
    tools: TweetsElaborationTools = TweetsElaborationTools.Defaults
) = with(database.createCollection<ElaboratedTweet>("tweets")) {
    getTweetsElaborationChunkedFlow(100, tools).onEach {
        insertMany(it)
    }
        .toList()
        .flatten()
        .sortedBy { it._id }
}

suspend fun CoroutineCollection<ElaboratedTweet>.insertTweets(tweets: List<ElaboratedTweet>) =
    insertMany(tweets)

suspend fun populateResources(database: CoroutineDatabase) {

    val listResourcesCollection =
        database.createCollection<ListResource>("listResources") {
            storageEngineOptions {
                autoIndexId = true
            }
        }

  val listResourceWithSentimentCollection =
      database.createCollection<ListResourceWithSentiment>("listResourceWithSentiment") {
        storageEngineOptions {
          autoIndexId = true
        }
      }

  val mapResourceCollection =
      database.createCollection<MapResource>("mapResource") {
        storageEngineOptions {
          autoIndexId = true
        }
      }

  listResourcesCollection.insertOne(
      ListResource(
          "punctuation",
          Resources.PUNCTUATION
      )
  )
  listResourcesCollection.insertOne(
      ListResource(
          "stop_words",
          Resources.STOPWORDS
      )
  )
  listResourcesCollection.insertOne(
      ListResource(
          "negation_words",
          Resources.NEGATION_WORDS
      )
  )

  mapResourceCollection.insertOne(
      MapResource(
          "contractions",
          Resources.CONTRACTIONS
      )
  )
  mapResourceCollection.insertOne(
      MapResource(
          "acronym",
          Resources.ACRONYMS
      )
  )

  Resources.LexicalData.Sentiments.Specific
      .forEach { (sem, data) ->
        listResourceWithSentimentCollection.insertOne(
            ListResourceWithSentiment(
                "EmoSN",
                data.EmoSN,
                sem
            )
        )
        listResourceWithSentimentCollection.insertOne(
            ListResourceWithSentiment(
                "NRC",
                data.NRC,
                sem
            )
        )
        listResourceWithSentimentCollection.insertOne(
            ListResourceWithSentiment(
                "SENTISENSE",
                data.SENTISENSE,
                sem
            )
        )
      }

}

suspend fun populateDatabase(database: CoroutineDatabase): List<ElaboratedTweet> {
//  populateResources(database)
  return populateTweets(database)
}

