package edu.unito.maadb.sql.analytics.core

import edu.unito.maadb.core.utils.SpecificSentiment
import kotlinx.serialization.Serializable

@Serializable
data class TweetsStatisticsResult(
    val sentiment: SpecificSentiment,
    val wordsWithOccurrences: Map<String, Int>,
    val newWordsNotInResources: List<String>
)

@Serializable
data class StatisticsResult(
    val sentiment: SpecificSentiment,
    val dataWithOccurrences: Map<String, Int>
)
