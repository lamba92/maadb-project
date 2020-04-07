package edu.unito.maadb.sql.daos

import edu.unito.maadb.sql.tables.TweetHashtagsTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TweetHashtagEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<TweetHashtagEntity>(TweetHashtagsTable)

    var hashtag by TweetHashtagsTable.hashtag
    var tweet by TweetEntity referencedOn TweetHashtagsTable.tweetId
    var count by TweetHashtagsTable.count
}
