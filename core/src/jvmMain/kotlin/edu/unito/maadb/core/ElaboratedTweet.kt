package edu.unito.maadb.core

import edu.unito.maadb.core.utils.Sentiment

data class ElaboratedTweet(
    val originalTweet: String,
    val sentiment: Sentiment,
    val tokenizedTweet: List<String>,
    val posTags: List<String>,
    val stemmedTweetWithOccurrences: Map<String, Int>,
    val hashtagsWithOccurrences: Map<String, Int>,
    val emojisWithOccurrences: Map<String, Int>,
    val emoticonsWithOccurrences: Map<String, Int>
)
