package edu.unito.maadb.datafeeder.nosql

import edu.unito.maadb.nosql.utils.populateDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


suspend fun main() {
    val MONGO_URL: String by System.getenv()
    val MONGO_DB_NAME: String by System.getenv()
    val db = KMongo.createClient(MONGO_URL)
        .getDatabase(MONGO_DB_NAME)
        .coroutine
    populateDatabase(db)
}
