package edu.unito.maadb.sql.daos

import edu.unito.maadb.core.utils.Sentiment
import edu.unito.maadb.sql.tables.TweetEmojisTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TweetEmojiEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<TweetEmojiEntity>(TweetEmojisTable)

    private var sentiment_ by TweetEmojisTable.sentiment

    var sentiment: Sentiment
        get() = Sentiment.valueOf(sentiment_)
        set(value) {
            sentiment_ = value.toString()
        }

    var tweet by TweetEntity referencedOn TweetEmojisTable.tweetId
    var emoji by TweetEmojisTable.emoji
    var count by TweetEmojisTable.count
}
