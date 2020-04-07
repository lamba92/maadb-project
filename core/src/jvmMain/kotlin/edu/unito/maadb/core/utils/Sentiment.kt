package edu.unito.maadb.core.utils

import edu.unito.maadb.core.utils.Sentiment.*

enum class Sentiment {
    ANGER, ANTICIPATION, DISGUST, HATE, FEAR, HOPE, JOY, LIKE, LOVE, SADNESS, SURPRISE, TRUST
}

val Sentiment.isNegative
    get() = when (this) {
        ANGER, ANTICIPATION, DISGUST, HATE, FEAR, SADNESS -> true
        else -> false
    }

val Sentiment.isPositive
    get() = !isNegative
