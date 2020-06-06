package edu.unito.maadb.analytics.core

import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.bg.CircleBackground
import com.kennycason.kumo.font.scale.SqrtFontScalar
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
@Location("{sentiment}")
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

fun defaultWordCloud(wordz: Map<String, Int>) = wordCloud {
    width = 1000
    height = 1000
    background = CircleBackground(500)
    fontScalar = SqrtFontScalar(5, 100)
    colorPalette {
        color(0x4055F1)
        color(0x408DF1)
        color(0x40AAF1)
        color(0x40C5F1)
        color(0x40D3F1)
        color(0xFFFFFF)
    }
    words = wordz.entries.sortedByDescending { it.value }.take(200).toMap()
}

@OptIn(ExperimentalStdlibApi::class)
private fun <T, R> Iterable<Map.Entry<T, R>>.toMap() = buildMap<T, R> {
    this@toMap.forEach { (t, r) -> put(t, r) }
}

fun wordCloud(action: WordCloudBuilder.() -> Unit): WordCloud =
    WordCloudBuilder().apply(action).build()

@DslMarker
annotation class WordCloudDSL
