package edu.unito.maadb.sql.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object TweetEmojisTable : LongIdTable("tweets_emojis_table") {
    val tweetId = reference("tweet_id", TweetsTable)
    val emoji = varchar("emoji", 5)
    val count = TweetHashtagsTable.integer("count")
}
