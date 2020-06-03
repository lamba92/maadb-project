package edu.unito.maadb.analytics.core

import edu.unito.maadb.core.utils.SpecificSentiment
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import kotlinx.serialization.Serializable

fun <K> MutableMap<K, Int>.merge(map: Map<K, Int>) = map.forEach { (k, v) ->
    this[k] = if (containsKey(k)) getValue(k) + v else v
}

fun <K> MutableMap<K, Int>.merge(element: Pair<K, Int>) {
    this[element.first] = if(containsKey(element.first)) getValue(element.first) + element.second else element.second
}

fun <K> Iterable<Map<K, Int>>.merge(accumulator: MutableMap<K, Int> = mutableMapOf()): Map<K, Int> {
    forEach { accumulator.merge(it) }
    return accumulator
}

@JvmName("mergeList")
fun <K> Iterable<Pair<K, Int>>.merge(accumulator: MutableMap<K, Int> = mutableMapOf()): Map<K, Int> {
    forEach { accumulator.merge(it) }
    return accumulator
}

@KtorExperimentalLocationsAPI
@Location("bySpecificSentiment/{sentiment}")
class SentimentLocation(sentiment: String) {
    val sentiment by lazy {
        SpecificSentiment.valueOf(sentiment.toUpperCase())
    }

    operator fun component1() = sentiment
}

@KtorExperimentalLocationsAPI
@Location("bySpecificSentiment/{sentiment}")
class SentimentPagedLocation(sentiment: String, val page: Int = 0, val pageSize: Int = 100) {
    val sentiment by lazy {
        SpecificSentiment.valueOf(sentiment.toUpperCase())
    }

    operator fun component1() = sentiment
    operator fun component2() = page
    operator fun component3() = pageSize
}

@Serializable
data class PagedData<T>(
    val data: T,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int
)
