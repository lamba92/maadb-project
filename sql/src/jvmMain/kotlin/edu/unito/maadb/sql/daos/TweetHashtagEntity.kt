package edu.unito.maadb.sql.daos

import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.sql.tables.TweetHashtagsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TweetHashtagEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<TweetHashtagEntity>(TweetHashtagsTable)

    private var sentiment_ by TweetHashtagsTable.sentiment

    var hashtag by TweetHashtagsTable.hashtag
    var tweet by TweetEntity referencedOn TweetHashtagsTable.tweetId
    var count by TweetHashtagsTable.count
    var sentiment: SpecificSentiment
        get() = SpecificSentiment.valueOf(sentiment_)
        set(value) {
            sentiment_ = value.toString()
        }

}
