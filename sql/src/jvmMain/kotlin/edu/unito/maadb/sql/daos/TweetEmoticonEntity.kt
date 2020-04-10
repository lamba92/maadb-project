package edu.unito.maadb.sql.daos

import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.sql.tables.TweetEmoticonsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TweetEmoticonEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<TweetEmoticonEntity>(TweetEmoticonsTable)

    private var sentiment_ by TweetEmoticonsTable.sentiment

    var sentiment: SpecificSentiment
        get() = SpecificSentiment.valueOf(sentiment_)
        set(value) {
            sentiment_ = value.toString()
        }

    var tweet by TweetEntity referencedOn TweetEmoticonsTable.tweetId
    var emoticon by TweetEmoticonsTable.emoticon
    var count by TweetEmoticonsTable.count
}

