package edu.unito.maadb.nosql.utils

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
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

data class ListResource(val name: String, val data: List<String>)

suspend fun populateResources(database: CoroutineDatabase) {

    val listResourcesCollection = database.createCollection<ListResource>("listResources")

    listResourcesCollection.insertOne(ListResource("punctuation", Resources.PUNCTUATION))

    Resources.STOPWORDS.forEach {

    }
    Resources.NEGATION_WORDS.forEach {

    }
    Resources.PUNCTUATION.forEach {

    }
    Resources.CONTRACTIONS.forEach { (a, b) ->

    }
    Resources.ACRONYMS.forEach { (a, b) ->

    }
    Resources.LexicalData.Sentiments.Specific.forEach { (sem, data) ->
        data.EmoSN.forEach { res ->

        }
        data.NRC.forEach { res ->

        }
        data.SENTISENSE.forEach { res ->

        }
    }
}

