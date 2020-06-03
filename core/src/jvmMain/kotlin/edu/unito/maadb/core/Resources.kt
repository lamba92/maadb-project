@file:Suppress("MemberVisibilityCanBePrivate")

package edu.unito.maadb.core

import edu.unito.maadb.core.utils.GenericSentiment
import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.core.utils.entriesOf
import java.io.File

object Resources {

  val PRE_TRAINED_POS_TAGGER_MODEL
    get() = getResource("Risorse lessicali/en-pos-maxent.bin")

  val STOPWORDS
    get() = getResource("Risorse lessicali/stopwords.txt")
        .readLines()

  val NEGATION_WORDS
    get() = getResource("Risorse lessicali/elenco-parole-che-negano-parole-successive.txt")
        .readLines()

  val ACRONYMS
    get() = getResource("Risorse lessicali/internet-acronyms.tsv")
        .readTsvPairs()
        .toMap()

  val CONTRACTIONS
    get() = getResource("Risorse lessicali/contractions.tsv")
        .readTsvPairs()
        .toMap()

  val PUNCTUATION
    get() = getResource("Risorse lessicali/punctuation-list.txt")
        .readLines()


  object Tweets : AbstractMap<SpecificSentiment, List<String>>() {

    val ANGER
      get() = getResource("Twitter messaggi/dataset_dt_anger_60k.txt")
          .readLines()
    val ANTICIPATION
      get() = getResource("Twitter messaggi/dataset_dt_anticipation_60k.txt")
          .readLines()
    val DISGUST
      get() = getResource("Twitter messaggi/dataset_dt_disgust_60k.txt")
          .readLines()
    val FEAR
      get() = getResource("Twitter messaggi/dataset_dt_fear_60k.txt")
          .readLines()
    val JOY
      get() = getResource("Twitter messaggi/dataset_dt_joy_60k.txt")
          .readLines()
    val SADNESS
      get() = getResource("Twitter messaggi/dataset_dt_sadness_60k.txt")
          .readLines()
    val SURPRISE
      get() = getResource("Twitter messaggi/dataset_dt_surprise_60k.txt")
          .readLines()
    val TRUST
      get() = getResource("Twitter messaggi/dataset_dt_trust_60k.txt")
          .readLines()

    override val entries
      get() = entriesOf(
          SpecificSentiment.ANGER to ANGER,
          SpecificSentiment.ANTICIPATION to ANTICIPATION,
          SpecificSentiment.DISGUST to DISGUST,
          SpecificSentiment.FEAR to FEAR,
          SpecificSentiment.JOY to JOY,
          SpecificSentiment.SADNESS to SADNESS,
          SpecificSentiment.SURPRISE to SURPRISE,
          SpecificSentiment.TRUST to TRUST
      )

  }

  object LexicalData {

    object Sentiments {

      object Specific : AbstractMap<SpecificSentiment, Specific.SpecificSentimentLexicalData>() {

        interface SpecificSentimentLexicalData {

          val EmoSN: List<String>
            get() = emptyList()

          val NRC: List<String>
            get() = emptyList()

          val SENTISENSE: List<String>
            get() = emptyList()

          val EVERY_RESOURCE
            get() = (EmoSN + NRC + SENTISENSE).toSet()
        }

        object Anger : SpecificSentimentLexicalData {
          override val EmoSN
            get() = getResource("Risorse lessicali/Anger/EmoSN_anger.txt")
                .readLines()
          override val NRC
            get() = getResource("Risorse lessicali/Anger/NRC_anger.txt")
                .readLines()
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Anger/sentisense_anger.txt")
                .readLines()
        }

        object Anticipation : SpecificSentimentLexicalData {
          override val NRC
            get() = getResource("Risorse lessicali/Anticipation/NRC_anticipation.txt")
                .readLines()
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Anticipation/sentisense_anticipation.txt")
                .readLines()
        }

        object Disgust : SpecificSentimentLexicalData {
          override val NRC
            get() = getResource("Risorse lessicali/Disgust-Hate/NRC_disgust.txt")
                .readLines()
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Disgust-Hate/sentisense_disgust.txt")
                .readLines()

        }

        object Hate : SpecificSentimentLexicalData {
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Disgust-Hate/sentisense_hate.txt")
                .readLines()

        }

        object Fear : SpecificSentimentLexicalData {
          override val NRC
            get() = getResource("Risorse lessicali/Fear/NRC_fear.txt")
                .readLines()
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Fear/sentisense_fear.txt")
                .readLines()

        }

        object Hope : SpecificSentimentLexicalData {
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Hope/sentisense_hope.txt")
                .readLines()

        }

        object Joy : SpecificSentimentLexicalData {
          override val EmoSN
            get() = getResource("Risorse lessicali/Joy/EmoSN_joy.txt")
                .readLines()
          override val NRC
            get() = getResource("Risorse lessicali/Joy/NRC_joy.txt")
                .readLines()
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Joy/sentisense_joy.txt")
                .readLines()

        }

        object Like : SpecificSentimentLexicalData {
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Like-Love/sentisense_like.txt")
                .readLines()
        }

        object Love : SpecificSentimentLexicalData {
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Like-Love/sentisense_love.txt")
                .readLines()
        }

        object Sadness : SpecificSentimentLexicalData {
          override val NRC
            get() = getResource("Risorse lessicali/Sadness/NRC_sadness.txt")
                .readLines()
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Sadness/sentisense_sadness.txt")
                .readLines()
        }

        object Surprise : SpecificSentimentLexicalData {
          override val NRC
            get() = getResource("Risorse lessicali/Surprise/NRC_surprise.txt")
                .readLines()
          override val SENTISENSE
            get() = getResource("Risorse lessicali/Surprise/sentisense_surprise.txt")
                .readLines()
        }

        object Trust : SpecificSentimentLexicalData {
          override val NRC
            get() = getResource("Risorse lessicali/Trust/NRC_trust.txt")
                .readLines()
        }

        override val entries
          get() = entriesOf(
              SpecificSentiment.ANGER to Anger,
              SpecificSentiment.ANTICIPATION to Anticipation,
              SpecificSentiment.DISGUST to Disgust,
              SpecificSentiment.HATE to Hate,
              SpecificSentiment.FEAR to Fear,
              SpecificSentiment.HOPE to Hope,
              SpecificSentiment.JOY to Joy,
              SpecificSentiment.LIKE to Like,
              SpecificSentiment.LOVE to Love,
              SpecificSentiment.SADNESS to Sadness,
              SpecificSentiment.SURPRISE to Surprise,
              SpecificSentiment.TRUST to Trust
          )

      }

      object Generic : AbstractMap<GenericSentiment, Generic.GenericSentimentLexicalData>() {

        interface GenericSentimentLexicalData {

          val GI: List<String>
            get() = emptyList()

          val HL: List<String>
            get() = emptyList()

          val EFFECTIVE_TERMS: List<String>
            get() = emptyList()

          val LIWC: List<String>
            get() = emptyList()

          val EVERY_RESOURCE
            get() = (GI + HL + EFFECTIVE_TERMS + LIWC).toSet()
        }

        object Positive : GenericSentimentLexicalData {
          override val GI
            get() = getResource("Risorse lessicali/Pos/GI_POS.txt")
                .readLines()
          override val HL
            get() = getResource("Risorse lessicali/Pos/HL-positives.txt")
                .readLines()
          override val EFFECTIVE_TERMS
            get() = getResource("Risorse lessicali/Pos/listPosEffTerms.txt")
                .readLines()
          override val LIWC
            get() = getResource("Risorse lessicali/Pos/LIWC-POS.txt")
                .readLines()

        }

        object Negative : GenericSentimentLexicalData {
          override val GI
            get() = getResource("Risorse lessicali/Neg/GI_NEG.txt")
                .readLines()
          override val HL
            get() = getResource("Risorse lessicali/Neg/HL-negatives.txt")
                .readLines()
          override val EFFECTIVE_TERMS
            get() = getResource("Risorse lessicali/Neg/listNegEffTerms.txt")
                .readLines()
          override val LIWC
            get() = getResource("Risorse lessicali/Neg/LIWC-NEG.txt")
                .readLines()
        }

        override val entries
          get() = entriesOf(
              GenericSentiment.POSITIVE to Positive,
              GenericSentiment.NEGATIVE to Negative
          )
      }

    }

    object ConScore {

      val AFFIN
        get() = getResource("Risorse lessicali/ConScore/afinn.txt")
            .mapLinesInt()

      object ANEW {

        val AROUSAL
          get() = getResource("Risorse lessicali/ConScore/anewAro_tab.tsv")
              .mapLinesFloat()
        val DOMINANCE
          get() = getResource("Risorse lessicali/ConScore/anewDom_tab.tsv")
              .mapLinesFloat()
        val PLEASANTNESS
          get() = getResource("Risorse lessicali/ConScore/anewPleas_tab.tsv")
              .mapLinesFloat()
      }

      object DAL {

        val ACTIVATION
          get() = getResource("Risorse lessicali/ConScore/Dal_Activ.csv")
              .mapLinesFloat()
        val IMAGERY
          get() = getResource("Risorse lessicali/ConScore/Dal_Imag.csv")
              .mapLinesFloat()
        val PLEASANTNESS
          get() = getResource("Risorse lessicali/ConScore/Dal_Pleas.csv")
              .mapLinesFloat()
      }

      private fun File.mapLinesInt(
          regex: Regex = Regex("(.*)\\s+(-?\\d{1,2})")
      ) = readLines().associate {
        regex.find(it)!!.groupValues.let { it[0] to it[1].toInt() }
      }

      private fun File.mapLinesFloat(
          regex: Regex = Regex("(.*)\\s+(-?\\d{1,2}\\.?\\d*)")
      ) = readLines().associate {
        regex.find(it)!!.groupValues.let { it[0] to it[1].toFloat() }
      }
    }
  }

  private fun File.readTsvPairs(): List<Pair<String, String>> {
    val reg = Regex("^([\\w'-\\?\\|]*)\\s+(.*)")
    return readLines().mapIndexed { index, line ->
      reg.find(line)?.groupValues?.let {
        it[1] to it[2]
      } ?: throw IllegalArgumentException("${reg.pattern} not found in #$index: $line")
    }
  }

  private fun getResource(name: String) =
      it.lamba.utils.getResource(name, classLoader = Resources::class.java.classLoader)

}
