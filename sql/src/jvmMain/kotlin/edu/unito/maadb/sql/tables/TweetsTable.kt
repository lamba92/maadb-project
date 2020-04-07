package edu.unito.maadb.sql.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object TweetsTable : LongIdTable("tweets_table") {
    val originalTweet = varchar("original_tweet", 280)
    val sentiment = varchar("sentiment", 15)
    val tokenizedTweet = varchar("tokenized_tweet", 1000)
    val posTags = varchar("pos_tags", 1000)
    val stemmedTweetWithOccurrences = varchar("stemmed_tweet_with_occurrences", 1000)
}
