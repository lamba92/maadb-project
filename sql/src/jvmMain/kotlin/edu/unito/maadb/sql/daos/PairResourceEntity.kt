package edu.unito.maadb.sql.daos

import edu.unito.maadb.sql.tables.SimplePairResourcesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PairResourceEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<PairResourceEntity>(SimplePairResourcesTable)

    var resource by SimplePairResourcesTable.resource
    var secondResource by SimplePairResourcesTable.secondResource
    var type by SimplePairResourcesTable.type
}
