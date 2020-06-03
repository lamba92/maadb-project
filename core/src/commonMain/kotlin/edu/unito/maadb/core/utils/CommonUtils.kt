package edu.unito.maadb.core.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

fun extractByRegexp(message: String, regex: Regex): Pair<Map<String, Int>, String> {
  val data = regex.findAll(message).map { it.value }
      .toList()
      .groupingBy { it }
      .eachCount()
  val cleaned = message.remove(data.keys)
  return data to cleaned
}

@ExperimentalStdlibApi
fun expandSlang(message: String, slangMap: Map<String, String>) =
    message.split(" ").joinToString(" ") { slangMap[it] ?: it }

@ExperimentalStdlibApi
fun removePunctuation(message: String, punctuation: List<String>) =
    message.remove(punctuation)

fun String.remove(vararg words: String) =
    remove(words.toList())

fun String.remove(words: Iterable<String>): String {
  var r = this
  words.forEach {
    r = r.remove(it)
  }
  return r
}

fun extractHashtags(tweet: String) =
    extractByRegexp(tweet, Regex("(#[\\w\\d]+)"))

fun String.remove(word: String) =
    replace(word, "")

fun <K, V> entriesOf(vararg entries: Pair<K, V>): Set<Map.Entry<K, V>> =
    entries.map {
      object : Map.Entry<K, V> {
        override val key
          get() = it.first
        override val value
          get() = it.second
      }
    }.toSet()

data class LongIndexedValue<T>(val index: Long, val value: T)

/**
 * Returns a flow that wraps each element into [IndexedValue], containing value and its index (starting from zero).
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.withLongIndex(): Flow<LongIndexedValue<T>> = flow {
  var index = 0L
  collect { value ->
    emit(LongIndexedValue(index++, value))
  }
}
