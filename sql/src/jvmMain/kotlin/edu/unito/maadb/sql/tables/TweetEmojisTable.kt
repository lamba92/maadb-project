package edu.unito.maadb.sql.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object TweetEmojisTable : LongIdTable("tweets_emojis_table") {
    val tweetId = reference("tweet_id", TweetsTable)
    val emoji = varchar("emoji", 15)
    val count = integer("count")
    val sentiment = varchar("sentiment", 15)
}
