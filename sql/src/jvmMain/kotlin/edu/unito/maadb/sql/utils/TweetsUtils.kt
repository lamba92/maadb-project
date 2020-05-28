package edu.unito.maadb.sql.utils

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.utils.TweetsElaborationTools
import edu.unito.maadb.core.utils.flatMapMergeIterable
import edu.unito.maadb.core.utils.getTweetsElaborationChunkedFlow
import edu.unito.maadb.sql.daos.TweetEmojiEntity
import edu.unito.maadb.sql.daos.TweetEmoticonEntity
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.daos.TweetHashtagEntity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun ElaboratedTweet.toEntity(): TweetEntity {
    val tweetEntity = TweetEntity.new(id) {
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
                id.value,
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
) = getTweetsElaborationChunkedFlow(2000, tools)
        .flatMapMergeIterable {
            newSuspendedTransaction(db = database) {
                it.map { it.toEntity() }
            }
        }
        .toList()
        .groupBy { it.sentiment }
