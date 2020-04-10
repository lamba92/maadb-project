package edu.unito.maadb.sql.daos

import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.sql.tables.TweetEmojisTable
import edu.unito.maadb.sql.tables.TweetEmoticonsTable
import edu.unito.maadb.sql.tables.TweetHashtagsTable
import edu.unito.maadb.sql.tables.TweetsTable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TweetEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<TweetEntity>(TweetsTable) {
        val serializer by lazy {
            Json(JsonConfiguration.Stable)
        }
    }

    private var sentiment_ by TweetsTable.sentiment
    private var tokenizedTweet_ by TweetsTable.tokenizedTweet
    private var posTags_ by TweetsTable.posTags
    private var stemmedTweetWithOccurrences_ by TweetsTable.stemmedTweetWithOccurrences

    var originalTweet by TweetsTable.originalTweet

    var sentiment: SpecificSentiment
        get() = SpecificSentiment.valueOf(sentiment_)
        set(value) {
            sentiment_ = value.toString()
        }
    var tokenizedTweet: List<String>
        get() = serializer.parse(String.serializer().list, tokenizedTweet_)
        set(value) {
            tokenizedTweet_ = serializer.stringify(String.serializer().list, value)
        }
    var posTags: List<String>
        get() = serializer.parse(String.serializer().list, posTags_)
        set(value) {
            posTags_ = serializer.stringify(String.serializer().list, value)
        }

    var stemmedTweetWithOccurrences: Map<String, Int>
        get() = serializer.parse(MapSerializer(String.serializer(), Int.serializer()), stemmedTweetWithOccurrences_)
        set(value) {
            stemmedTweetWithOccurrences_ =
                serializer.stringify(MapSerializer(String.serializer(), Int.serializer()), value)
        }

    val hashtags
        get() = TweetHashtagEntity.find { TweetHashtagsTable.tweetId eq id }

    val emojis
        get() = TweetEmojiEntity.find { TweetEmojisTable.tweetId eq id }

    val emoticons
        get() = TweetEmoticonEntity.find { TweetEmoticonsTable.tweetId eq id }

}
