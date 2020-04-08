package edu.unito.maadb.sql.analytics.core

import edu.unito.maadb.core.utils.Sentiment
import kotlinx.serialization.Serializable

@Serializable
data class TweetsStatisticsResult(
    val sentiment: Sentiment,
    val wordsWithOccurrences: Map<String, Int>,
    val newWordsNotInResources: List<String>
)

@Serializable
data class HashtagsStatisticsResult(
    val sentiment: Sentiment,
    val hashtagsWithOccurrences: Map<String, Int>
)
