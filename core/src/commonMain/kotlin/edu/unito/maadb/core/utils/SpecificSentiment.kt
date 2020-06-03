package edu.unito.maadb.core.utils

import edu.unito.maadb.core.utils.SpecificSentiment.*


enum class GenericSentiment {
    POSITIVE, NEGATIVE
}

enum class SpecificSentiment {
    ANGER, ANTICIPATION, DISGUST, HATE, FEAR, HOPE, JOY, LIKE, LOVE, SADNESS, SURPRISE, TRUST
}

fun SpecificSentiment.asGeneric() = when (this) {
    ANGER, ANTICIPATION, DISGUST, HATE, FEAR, SADNESS -> GenericSentiment.NEGATIVE
    else -> GenericSentiment.POSITIVE
}
