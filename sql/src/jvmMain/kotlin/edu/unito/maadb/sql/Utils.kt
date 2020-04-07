package edu.unito.maadb.sql

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import edu.unito.maadb.core.utils.TweetsElaborationTools
import edu.unito.maadb.core.utils.elaborateTweet
import edu.unito.maadb.sql.daos.TweetEmojiEntity
import edu.unito.maadb.sql.daos.TweetEmoticonEntity
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.daos.TweetHashtagEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun ElaboratedTweet.toEntity(): TweetEntity {
    val tweetEntity = TweetEntity.new {
        originalTweet = this@toEntity.originalTweet
        sentiment = this@toEntity.sentiment
        tokenizedTweet = this@toEntity.tokenizedTweet
        posTags = this@toEntity.posTags
        stemmedTweetWithOccurrences = this@toEntity.stemmedTweetWithOccurrences
    }

    hashtagsWithOccurrences.forEach { (ht, cnt) ->
        TweetHashtagEntity.new {
            tweet = tweetEntity
            hashtag = ht
            count = cnt
        }
    }

    emojisWithOccurrences.forEach { (emj, cnt) ->
        TweetEmojiEntity.new {
            tweet = tweetEntity
            emoji = emj
            count = cnt
        }
    }

    emoticonsWithOccurrences.forEach { (emt, cnt) ->
        TweetEmoticonEntity.new {
            tweet = tweetEntity
            emoticon = emt
            count = cnt
        }
    }

    return tweetEntity
}

fun TweetEntity.toModel() =
    ElaboratedTweet(
        originalTweet,
        sentiment,
        tokenizedTweet,
        posTags,
        stemmedTweetWithOccurrences,
        hashtags.associate { it.hashtag to it.count },
        emojis.associate { it.emoji to it.count },
        emoticons.associate { it.emoticon to it.count }
    )

@ExperimentalStdlibApi
suspend fun populateDatabase(database: Database, tools: TweetsElaborationTools) {

    Resources.Tweets.forEach { (sentiment, tweets) ->
        val elaboratedTweets = tweets.map {
            with(tools) {
                elaborateTweet(
                    sentiment, it, punctuation, slangMap,
                    stopwords, stemmer, posTagger, tokenizer
                )
            }
        }

        newSuspendedTransaction(db = database) {
            elaboratedTweets.forEach {
                it.toEntity()
            }
        }
    }

}
