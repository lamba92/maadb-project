package edu.unito.maadb.core.utils

import com.vdurmont.emoji.EmojiParser
import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import emoji4j.EmojiManager
import emoji4j.EmojiUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import opennlp.tools.postag.POSTagger

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

fun extractEmoticons(message: String) =
    extractByRegexp(message, EmojiManager.getEmoticonRegexPattern().toRegex())

fun extractEmojis(message: String) = Pair(
    EmojiParser.extractEmojis(message)
        .groupingBy { it!! }
        .eachCount(),
    EmojiUtils.removeAllEmojis(message)!!
)

fun extractHashtags(tweet: String) =
    extractByRegexp(tweet, Regex("(#[\\w\\d]+)"))

fun String.remove(word: String) =
    replace(word, "")

fun POSTagger.tag(sentence: List<String>): List<String> =
    tag(sentence.toTypedArray()).toList()

@ExperimentalStdlibApi
fun elaborateTweet(
    id: Long,
    sentiment: SpecificSentiment,
    message: String,
    tools: TweetsElaborationTools
): ElaboratedTweet {

    val (hashtags, tweetWithoutHashtags) = extractHashtags(message)
    val (emojis, tweetWithoutHashtagsAndEmojis) = extractEmojis(tweetWithoutHashtags)
    val (emoticons, tweetWithoutHashtagsAndEmojisAndEmoticons) = extractEmoticons(tweetWithoutHashtagsAndEmojis)

  val cleanMessage = tweetWithoutHashtagsAndEmojisAndEmoticons.remove("URL", "USERNAME")
      .toLowerCase()
      .let { expandSlang(it, tools.slangMap) }
      .replace("$", "DOLLAR_SYMBOL")

  val tokenizedMessage: List<String> = removePunctuation(cleanMessage, tools.punctuation)
      .let { tools.tokenizer.tokenize(it) }
      .toList()

  val pos = tools.posTagger.tag(tokenizedMessage)

  val finalMessage = tokenizedMessage.map { tools.stemmer.stem(it).toString() }
      .filter { it !in tools.stopwords }
      .groupingBy { it }
      .eachCount()

  return ElaboratedTweet(
      id,
      message,
      sentiment,
      tokenizedMessage,
      pos,
      finalMessage,
      hashtags,
      emojis,
      emoticons
  )
}

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

@OptIn(ExperimentalStdlibApi::class, FlowPreview::class, ExperimentalCoroutinesApi::class)
fun getTweetsElaborationChunkedFlow(chunkSize: Int = 100, tools: TweetsElaborationTools) =
    Resources.Tweets.entries.asFlow()
        .flatMapMergeIterable { (sentiment, tweets) ->
          tweets.map { sentiment to it }
        }
        .withLongIndex()
        .map { (index, data) ->
            val (sentiment, tweet) = data
          elaborateTweet(
              index,
              sentiment,
              tweet,
              tools
          )
        }
        .chunked(chunkSize)

@OptIn(FlowPreview::class)
fun <T, R> Flow<T>.flatMapMergeIterable(
    transform: suspend (T) -> Iterable<R>
): Flow<R> = flatMapMerge { transform(it).asFlow() }
