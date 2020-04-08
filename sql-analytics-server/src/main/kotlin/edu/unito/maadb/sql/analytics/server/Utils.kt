package edu.unito.maadb.sql.analytics.server

import edu.unito.maadb.core.utils.Sentiment
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location

fun <K> MutableMap<K, Int>.merge(map: Map<K, Int>) = map.forEach { (k, v) ->
    this[k] = if (containsKey(k)) getValue(k) + v else v
}

fun <K> Iterable<Map<K, Int>>.merge(accumulator: MutableMap<K, Int> = mutableMapOf()): Map<K, Int> {
    forEach { accumulator.merge(it) }
    return accumulator
}

@KtorExperimentalLocationsAPI
@Location("bySpecificEmotion/{emotion}")
class EmotionLocation(emotion: String) {
    val emotion by lazy { Sentiment.valueOf(emotion.toUpperCase()) }
}
