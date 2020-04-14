package edu.unito.maadb.nosql.utils

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.core.utils.TweetsElaborationTools
import edu.unito.maadb.core.utils.getTweetElaborationFlow
import edu.unito.maadb.nosql.dsl.createCollection
import edu.unito.maadb.nosql.dsl.storageEngineOptions
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onEach
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

@FlowPreview
@ExperimentalStdlibApi
suspend fun populateTweets(
    database: CoroutineDatabase,
    tools: TweetsElaborationTools
): CoroutineCollection<ElaboratedTweet> {

    val collection = database.createCollection<ElaboratedTweet>("tweets") {
        storageEngineOptions {
            autoIndexId = true
        }
    }

    getTweetElaborationFlow(tools).onEach {
        collection.insertMany(it)
    }

    return collection

}

data class ListResource(val type: String, val data: List<String>)
data class ListResourceWithSentiment(val type: String, val data: List<String>, val sentiment: SpecificSentiment)

data class MapResource(val type: String, val data:Map<String,String>)

suspend fun populateResources(database: CoroutineDatabase) {

    val listResourcesCollection =
            database.createCollection<ListResource>("listResources")

    val listResourceWithSentimentCollection =
            database.createCollection<ListResourceWithSentiment>("listResourceWithSentiment")

    val mapResourceCollection =
            database.createCollection<MapResource>("mapResource")

    listResourcesCollection.insertOne(ListResource("punctuation", Resources.PUNCTUATION))
    listResourcesCollection.insertOne(ListResource("stop_words", Resources.STOPWORDS))
    listResourcesCollection.insertOne(ListResource("negation_words", Resources.NEGATION_WORDS))
    mapResourceCollection.insertOne(MapResource("contractions", Resources.CONTRACTIONS))
    mapResourceCollection.insertOne(MapResource("acronym", Resources.ACRONYMS))

    Resources.LexicalData.Sentiments.Specific.forEach { (sem, data) ->
        
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

