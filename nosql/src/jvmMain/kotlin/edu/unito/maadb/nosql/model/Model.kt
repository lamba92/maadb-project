package edu.unito.maadb.nosql.model

import edu.unito.maadb.core.utils.SpecificSentiment
import kotlinx.serialization.Serializable

@Serializable
data class ListResource(
    val type: String,
    val data: List<String>
)

@Serializable
data class ListResourceWithSentiment(
    val type: String,
    val data: List<String>,
    val sentiment: SpecificSentiment
)

@Serializable
data class MapResource(
    val type: String,
    val data: Map<String, String>
)
