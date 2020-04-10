package edu.unito.maadb.sql.utils

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import edu.unito.maadb.core.utils.TweetsElaborationTools
import edu.unito.maadb.core.utils.elaborateTweet
import edu.unito.maadb.sql.daos.TweetEmojiEntity
import edu.unito.maadb.sql.daos.TweetEmoticonEntity
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.daos.TweetHashtagEntity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
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
            sentiment = this@toEntity.sentiment
        }
    }

    emojisWithOccurrences.forEach { (emj, cnt) ->
        TweetEmojiEntity.new {
            tweet = tweetEntity
            emoji = emj
            count = cnt
            sentiment = this@toEntity.sentiment
        }
    }

    emoticonsWithOccurrences.forEach { (emt, cnt) ->
        TweetEmoticonEntity.new {
            tweet = tweetEntity
            emoticon = emt
            count = cnt
            sentiment = this@toEntity.sentiment
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

@FlowPreview
@ExperimentalStdlibApi
suspend fun populateTweets(
    tools: TweetsElaborationTools,
    database: Database
) = Resources.Tweets.entries.asFlow()
    .flatMapMergeIterable { (sentiment, tweets) ->
        tweets.map { sentiment to it }
    }
    .map { (sentiment, tweet) ->
        elaborateTweet(
            sentiment,
            tweet,
            tools
        )
    }
    .chunked(100)
    .flatMapMergeIterable {
        newSuspendedTransaction(db = database) {
            it.map { it.toEntity() }
        }
    }
    .toList()
    .groupBy { it.sentiment }
