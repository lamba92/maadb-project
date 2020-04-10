package edu.unito.maadb.sql.tables

abstract class WithSentimentPairTable(tableName: String) : PairTable(tableName) {
    val sentiment = varchar("sentiment", 25)
}
