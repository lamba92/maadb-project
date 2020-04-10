package edu.unito.maadb.sql.daos

import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.sql.tables.WithSentimentResourcesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class WithSentimentResourceEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<WithSentimentResourceEntity>(WithSentimentResourcesTable)

    var resource by WithSentimentResourcesTable.resource
    var type by WithSentimentResourcesTable.type

    private var sentiment_ by WithSentimentResourcesTable.sentiment

    var sentiment: SpecificSentiment
        get() = SpecificSentiment.valueOf(sentiment_)
        set(value) {
            sentiment_ = value.toString()
        }
}
