package edu.unito.maadb.core.utils

import edu.unito.maadb.core.Resources
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTagger
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.stemmer.Stemmer
import opennlp.tools.stemmer.snowball.SnowballStemmer
import opennlp.tools.tokenize.Tokenizer
import opennlp.tools.tokenize.WhitespaceTokenizer

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
                POSTaggerME(
                    POSModel(
                        Resources.PRE_TRAINED_POS_TAGGER_MODEL
                    )
                ),
                WhitespaceTokenizer.INSTANCE!!
            )
    }
}
