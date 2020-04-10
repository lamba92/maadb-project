package edu.unito.maadb.sql.tables

import org.jetbrains.exposed.dao.id.LongIdTable

abstract class BaseResourceTable(tableName: String) : LongIdTable(tableName) {
    val resource = varchar("resource", 100)
    val type = varchar("type", 25)
}
