@file:Suppress("MemberVisibilityCanBePrivate")

package edu.unito.maadb.core

import edu.unito.maadb.core.utils.Sentiment
import edu.unito.maadb.core.utils.entriesOf
import it.lamba.utils.getResource
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

    private fun File.readTsvPairs(): List<Pair<String, String>> {
        val reg = Regex("(.*)\\t(.*)")
        return readLines().map {
            reg.find(it)!!.groupValues.let { it[0] to it[1] }
        }
    }

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

    object Tweets : AbstractMap<Sentiment, List<String>>() {

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
                Sentiment.ANGER to ANGER,
                Sentiment.ANTICIPATION to ANTICIPATION,
                Sentiment.DISGUST to DISGUST,
                Sentiment.FEAR to FEAR,
                Sentiment.JOY to JOY,
                Sentiment.SADNESS to SADNESS,
                Sentiment.SURPRISE to SURPRISE,
                Sentiment.TRUST to TRUST,
            )

    }

    object LexicalData {

        object Emotions {

            object Specific : AbstractMap<Sentiment, Set<String>>() {

                object Anger {
                    val EmoSN
                        get() = getResource("Risorse lessicali/Anger/EmoSN_anger.txt")
                            .readLines()
                    val NRC
                        get() = getResource("Risorse lessicali/Anger/NRC_anger.txt")
                            .readLines()
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Anger/sentisense_anger.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = (EmoSN + NRC + SENTISENSE).toSet()

                }

                object Anticipation {
                    val NRC
                        get() = getResource("Risorse lessicali/Anticipation/NRC_anticipation.txt")
                            .readLines()
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Anticipation/sentisense_anticipation.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = (NRC + SENTISENSE).toSet()
                }

                object Disgust {
                    val NRC
                        get() = getResource("Risorse lessicali/Disgust-Hate/NRC_disgust.txt")
                            .readLines()
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Disgust-Hate/sentisense_disgust.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = (NRC + SENTISENSE).toSet()
                }

                object Hate {
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Disgust-Hate/sentisense_hate.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = SENTISENSE.toSet()
                }

                object Fear {
                    val NRC
                        get() = getResource("Risorse lessicali/Fear/NRC_fear.txt")
                            .readLines()
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Fear/sentisense_fear.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = (NRC + SENTISENSE).toSet()
                }

                object Hope {
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Hope/sentisense_hope.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = SENTISENSE.toSet()
                }

                object Joy {
                    val EmoSN
                        get() = getResource("Risorse lessicali/Joy/EmoSN_joy.txt")
                            .readLines()
                    val NRC
                        get() = getResource("Risorse lessicali/Joy/NRC_joy.txt")
                            .readLines()
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Joy/sentisense_joy.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = (EmoSN + NRC + SENTISENSE).toSet()
                }

                object Like {
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Like-Love/sentisense_like.txt")
                            .readLines()
                    val EVERY_RESOURCE
                        get() = SENTISENSE.toSet()
                }

                object Love {
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Like-Love/sentisense_love.txt")
                            .readLines()
                    val EVERY_RESOURCE
                        get() = SENTISENSE.toSet()
                }

                object Sadness {
                    val NRC
                        get() = getResource("Risorse lessicali/Sadness/NRC_sadness.txt")
                            .readLines()
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Sadness/sentisense_sadness.txt")
                            .readLines()
                    val EVERY_RESOURCE
                        get() = (NRC + SENTISENSE).toSet()
                }

                object Surprise {
                    val NRC
                        get() = getResource("Risorse lessicali/Surprise/NRC_surprise.txt")
                            .readLines()
                    val SENTISENSE
                        get() = getResource("Risorse lessicali/Surprise/sentisense_surprise.txt")
                            .readLines()
                    val EVERY_RESOURCE
                        get() = (NRC + SENTISENSE).toSet()
                }

                object Trust {
                    val NRC
                        get() = getResource("Risorse lessicali/Trust/NRC_trust.txt")
                            .readLines()
                    val EVERY_RESOURCE
                        get() = NRC.toSet()
                }

                override val entries
                    get() = entriesOf(
                        Sentiment.ANGER to Anger.EVERY_RESOURCE,
                        Sentiment.ANTICIPATION to Anticipation.EVERY_RESOURCE,
                        Sentiment.DISGUST to Disgust.EVERY_RESOURCE,
                        Sentiment.HATE to Hate.EVERY_RESOURCE,
                        Sentiment.FEAR to Fear.EVERY_RESOURCE,
                        Sentiment.HOPE to Hope.EVERY_RESOURCE,
                        Sentiment.JOY to Joy.EVERY_RESOURCE,
                        Sentiment.LIKE to Like.EVERY_RESOURCE,
                        Sentiment.LOVE to Love.EVERY_RESOURCE,
                        Sentiment.SADNESS to Sadness.EVERY_RESOURCE,
                        Sentiment.SURPRISE to Surprise.EVERY_RESOURCE,
                        Sentiment.TRUST to Trust.EVERY_RESOURCE
                    )

            }

            object Generic {

                object Positive {
                    val GI
                        get() = getResource("Risorse lessicali/Pos/GI_POS.txt")
                            .readLines()
                    val HL
                        get() = getResource("Risorse lessicali/Pos/HL-positives.txt")
                            .readLines()
                    val EFFECTIVE_TERMS
                        get() = getResource("Risorse lessicali/Pos/listPosEffTerms.txt")
                            .readLines()
                    val LIWC
                        get() = getResource("Risorse lessicali/Pos/LIWC-POS.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = (GI + HL + EFFECTIVE_TERMS + LIWC).toSet()
                }

                object Negative {
                    val GI
                        get() = getResource("Risorse lessicali/Neg/GI_NEG.txt")
                            .readLines()
                    val HL
                        get() = getResource("Risorse lessicali/Neg/HL-negatives.txt")
                            .readLines()
                    val EFFECTIVE_TERMS
                        get() = getResource("Risorse lessicali/Neg/listNegEffTerms.txt")
                            .readLines()
                    val LIWC
                        get() = getResource("Risorse lessicali/Neg/LIWC-NEG.txt")
                            .readLines()

                    val EVERY_RESOURCE
                        get() = (GI + HL + EFFECTIVE_TERMS + LIWC).toSet()
                }

            }

        }

        object ConScore {

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
        }
    }

}
