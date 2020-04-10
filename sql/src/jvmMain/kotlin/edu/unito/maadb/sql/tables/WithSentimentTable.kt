package edu.unito.maadb.sql.tables

abstract class WithSentimentTable(tableName: String) : BaseResourceTable(tableName) {
    val sentiment = varchar("sentiment", 25)
}
