package edu.unito.maadb.sql.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object TweetEmoticonsTable : LongIdTable("tweets_emoticons_table") {
    val tweetId = reference("tweet_id", TweetsTable)
    val emoticon = varchar("emoticon", 15)
    val count = integer("count")
    val sentiment = varchar("sentiment", 15)
}
