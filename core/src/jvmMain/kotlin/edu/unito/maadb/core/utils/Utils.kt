package edu.unito.maadb.core.utils

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.Resources
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTagger
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.stemmer.Stemmer
import opennlp.tools.stemmer.snowball.SnowballStemmer
import opennlp.tools.tokenize.Tokenizer
import opennlp.tools.tokenize.WhitespaceTokenizer

fun extractByRegexp(message: String, regex: Regex) =
    regex.findAll(message)
        .map { it.value }
        .groupingBy { it }
        .eachCount()

@ExperimentalStdlibApi
fun expandSlang(message: String, slangMap: Map<String, String>) =
    message.split(" ")
        .joinToString(" ") { slangMap[it] ?: it }

@ExperimentalStdlibApi
fun removePunctuation(message: String, punctuation: List<String>): String {
    var m = message
    punctuation.forEach {
        m = m.replace(it, " ")
    }
    return m
}

fun extractEmoticons(message: String) =
    extractByRegexp(message, emoticonsRegexp)

fun extractEmojis(message: String) =
    extractByRegexp(message, emojisRegexp)

fun extractHashtags(tweet: String) =
    tweet.split(" ").filter { it.startsWith("#") }

fun cleanTweet(message: String) =
    message.removeWord("URL").removeWord("USERNAME")

fun String.removeWord(word: String) =
    split(" ").filter { it != word }.joinToString(" ")

//val posTagger = POSTaggerME(POSModel(getResource("Risorse lessicali/en-pos-maxent.bin")))

fun POSTagger.tag(sentence: List<String>): List<String> =
    tag(sentence.toTypedArray()).toList()

@ExperimentalStdlibApi
fun elaborateTweet(
    sentiment: Sentiment,
    message: String,
    punctuation: List<String>,
    slangMap: Map<String, String>,
    stopwords: List<String>,
    stemmer: Stemmer,
    posTagger: POSTagger,
    tokenizer: Tokenizer
): ElaboratedTweet {

    val cleanMessage = cleanTweet(message)
        .toLowerCase()
        .let { expandSlang(it, slangMap) }

    val hashtags = extractHashtags(cleanMessage)
        .groupingBy { it }
        .eachCount()
    val emojis = extractEmojis(cleanMessage)
    val emoticons = extractEmoticons(cleanMessage)
    val tokenizedMessage: List<String> = cleanMessage.split(" ")
        .filter { it !in hashtags || it !in emojis.keys || it !in emoticons.keys }
        .joinToString(" ")
        .let { removePunctuation(it, punctuation) }
        .let { tokenizer.tokenize(it) }
        .toList()

    val pos = posTagger.tag(tokenizedMessage)

    val finalMessage = tokenizedMessage.map { stemmer.stem(it).toString() }
        .filter { it !in stopwords }
        .groupingBy { it }
        .eachCount()

    return ElaboratedTweet(
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

val emojisRegexp by lazy {
    Regex(
        "(\\u00a9|\\u00ae|[\\u2000-\\u3300]|\\ud83c[\\ud000-" +
                "\\udfff]|\\ud83d[\\ud000-\\udfff]|\\ud83e[\\ud000-\\udfff])"
    )
}
val emoticonsRegexp by lazy {
    Regex(
        "(\\:\\w+\\:|\\<[\\/\\\\]?3|[\\(\\)\\\\\\D|\\*\\\$]" +
                "[\\-\\^]?[\\:\\;\\=]|[\\:\\;\\=B8][\\-\\^]?[3DOPp\\@\\\$\\*\\\\\\)\\(\\/\\|])" +
                "(?=\\s|[\\!\\.\\?]|\$)"
    )
}

fun elaborateSentimentResource(resource: List<String>) =
    resource.filter { "_" !in it }
        .groupingBy { it }
        // TODO percentage
        .eachCount()

fun <K, V> entriesOf(vararg entries: Pair<K, V>): Set<Map.Entry<K, V>> =
    entries.map {
        object : Map.Entry<K, V> {
            override val key
                get() = it.first
            override val value
                get() = it.second
        }
    }.toSet()

data class TweetsElaborationTools(
    val punctuation: List<String>,
    val slangMap: Map<String, String>,
    val stopwords: List<String>,
    val stemmer: Stemmer,
    val posTagger: POSTagger,
    val tokenizer: Tokenizer
) {
    companion object {
        val Defaults
            get() = TweetsElaborationTools(
                Resources.PUNCTUATION,
                Resources.ACRONYMS,
                Resources.STOPWORDS,
                SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH),
                POSTaggerME(POSModel(Resources.PRE_TRAINED_POS_TAGGER_MODEL)),
                WhitespaceTokenizer.INSTANCE!!
            )
    }
}
