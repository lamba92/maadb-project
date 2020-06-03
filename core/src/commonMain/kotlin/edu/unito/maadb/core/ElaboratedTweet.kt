package edu.unito.maadb.core

import edu.unito.maadb.core.utils.SpecificSentiment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ElaboratedTweet(
    val _id: Long,
    val originalTweet: String,
    val sentiment: SpecificSentiment,
    val tokenizedTweet: List<String>,
    val posTags: List<String>,
    val stemmedTweetWithOccurrences: Map<String, Int>,
    val hashtagsWithOccurrences: Map<String, Int>,
    val emojisWithOccurrences: Map<String, Int>,
    val emoticonsWithOccurrences: Map<String, Int>
)
