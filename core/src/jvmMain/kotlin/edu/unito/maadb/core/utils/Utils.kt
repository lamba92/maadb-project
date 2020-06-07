package edu.unito.maadb.core.utils

import com.vdurmont.emoji.EmojiParser
import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import emoji4j.EmojiManager
import emoji4j.EmojiUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import opennlp.tools.postag.POSTagger

fun extractEmoticons(message: String) =
    extractByRegexp(message, EmojiManager.getEmoticonRegexPattern().toRegex())

fun extractEmojis(message: String) = Pair(
    EmojiParser.extractEmojis(message)
        .groupingBy { it!! }
        .eachCount(),
    EmojiUtils.removeAllEmojis(message)!!
)

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

@OptIn(ExperimentalStdlibApi::class, FlowPreview::class, ExperimentalCoroutinesApi::class)
fun getTweetsElaborationChunkedFlow(chunkSize: Int = 100, tools: TweetsElaborationTools = TweetsElaborationTools.Defaults) =
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
