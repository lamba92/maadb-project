package edu.unito.maadb.sql.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object TweetHashtagsTable : LongIdTable("tweets_hashtags_table") {
    val tweetId = reference("tweet_id", TweetsTable)
    val hashtag = varchar("hashtag", 280)
    val count = integer("count")
}
