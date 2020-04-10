package edu.unito.maadb.sql.daos

import edu.unito.maadb.sql.tables.SimpleResourcesTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SimpleResourceEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<SimpleResourceEntity>(SimpleResourcesTable)

    var resource by SimpleResourcesTable.resource
    var type by SimpleResourcesTable.type
}
