package edu.unito.maadb.sql.tables

abstract class PairTable(tableName: String) : BaseResourceTable(tableName) {
    val secondResource = varchar("second_resource", 100)
}
