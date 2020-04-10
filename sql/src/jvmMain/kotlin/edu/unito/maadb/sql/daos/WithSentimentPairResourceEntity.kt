package edu.unito.maadb.sql.daos

import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.sql.tables.WithSentimentPairResourcesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.id.EntityID

class WithSentimentPairResourceEntity(id: EntityID<Long>) : LongEntity(id) {

    var resource by WithSentimentPairResourcesTable.resource
    var type by WithSentimentPairResourcesTable.type
    var secondResource by WithSentimentPairResourcesTable.secondResource

    private var sentiment_ by WithSentimentPairResourcesTable.sentiment

    var sentiment: SpecificSentiment
        get() = SpecificSentiment.valueOf(sentiment_)
        set(value) {
            sentiment_ = value.toString()
        }
}
