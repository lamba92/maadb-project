package edu.unito.maadb.sql

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import edu.unito.maadb.core.utils.Sentiment
import edu.unito.maadb.core.utils.TweetsElaborationTools
import edu.unito.maadb.core.utils.elaborateTweet
import edu.unito.maadb.sql.daos.TweetEmojiEntity
import edu.unito.maadb.sql.daos.TweetEmoticonEntity
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.daos.TweetHashtagEntity
import edu.unito.maadb.sql.tables.TweetEmojisTable
import edu.unito.maadb.sql.tables.TweetEmoticonsTable
import edu.unito.maadb.sql.tables.TweetHashtagsTable
import edu.unito.maadb.sql.tables.TweetsTable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
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

@OptIn(ExperimentalStdlibApi::class, FlowPreview::class)
suspend fun populateDatabase(
    database: Database,
    tools: TweetsElaborationTools = TweetsElaborationTools.Defaults
): Map<Sentiment, List<TweetEntity>> {

    newSuspendedTransaction(db = database) {
        SchemaUtils.createMissingTablesAndColumns(
            TweetsTable,
            TweetEmojisTable,
            TweetEmoticonsTable,
            TweetHashtagsTable
        )
    }

    return Resources.Tweets.entries.asFlow()
        .flatMapMergeIterable { (sentiment, tweets) ->
            tweets.map { sentiment to it }
        }
        .map { (sentiment, tweet) -> elaborateTweet(sentiment, tweet, tools) }
        .chunked(100)
        .flatMapMergeIterable {
            newSuspendedTransaction(db = database) {
                it.map { it.toEntity() }
            }
        }
        .toList()
        .groupBy { it.sentiment }

}

fun populateDatabaseBlocking(
    database: Database,
    tools: TweetsElaborationTools = TweetsElaborationTools.Defaults
) = runBlocking { populateDatabase(database, tools) }

@OptIn(FlowPreview::class)
fun <T, R> Flow<T>.flatMapMergeIterable(
    transform: suspend (T) -> Iterable<R>
): Flow<R> = flatMapMerge { transform(it).asFlow() }
