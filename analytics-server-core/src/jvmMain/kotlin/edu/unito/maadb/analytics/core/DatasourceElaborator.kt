package edu.unito.maadb.analytics.core

import edu.unito.maadb.core.ElaboratedTweet
import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.sql.analytics.core.StatisticsResult
import edu.unito.maadb.sql.analytics.core.TweetsStatisticsResult

interface DatasourceElaborator {

  suspend fun statsTweets(sentiment: SpecificSentiment): TweetsStatisticsResult
  suspend fun statsHashtags(sentiment: SpecificSentiment): StatisticsResult
  suspend fun statsEmoticons(sentiment: SpecificSentiment): StatisticsResult
  suspend fun statsEmojis(sentiment: SpecificSentiment): StatisticsResult

  suspend fun tweets(sentiment: SpecificSentiment, page: Int, pageSize: Int): PagedData<List<ElaboratedTweet>>

}
