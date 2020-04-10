package edu.unito.maadb.sql.utils

import edu.unito.maadb.core.Resources
import edu.unito.maadb.sql.daos.PairResourceEntity
import edu.unito.maadb.sql.daos.SimpleResourceEntity
import edu.unito.maadb.sql.daos.WithSentimentResourceEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun populateResources(database: Database) {

    suspend fun <T> t(s: suspend Transaction.() -> T) =
        newSuspendedTransaction(db = database, statement = s)

    t {
        Resources.STOPWORDS.forEach {
            SimpleResourceEntity.new {
                resource = it
                type = "stopword"
            }
        }
        Resources.NEGATION_WORDS.forEach {
            SimpleResourceEntity.new {
                resource = it
                type = "negation_word"
            }
        }
        Resources.PUNCTUATION.forEach {
            SimpleResourceEntity.new {
                resource = it
                type = "punctuation"
            }
        }
    }
    t {
        Resources.CONTRACTIONS.forEach { (a, b) ->
            PairResourceEntity.new {
                resource = a
                secondResource = b
                type = "contractions"
            }
        }
        Resources.ACRONYMS.forEach { (a, b) ->
            PairResourceEntity.new {
                resource = a
                secondResource = b
                type = "acronyms"
            }
        }
    }
    Resources.LexicalData.Sentiments.Specific.forEach { (sem, data) ->
        t {
            data.EmoSN.forEach { res ->
                WithSentimentResourceEntity.new {
                    resource = res
                    type = "EmoSN"
                    sentiment = sem
                }
            }
            data.NRC.forEach { res ->
                WithSentimentResourceEntity.new {
                    resource = res
                    type = "NRC"
                    sentiment = sem
                }
            }
            data.SENTISENSE.forEach { res ->
                WithSentimentResourceEntity.new {
                    resource = res
                    type = "SENTISENSE"
                    sentiment = sem
                }
            }
        }
    }
}
