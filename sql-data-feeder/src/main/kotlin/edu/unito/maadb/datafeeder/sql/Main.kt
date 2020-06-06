package edu.unito.maadb.datafeeder.sql

import edu.unito.maadb.sql.utils.populateDatabase
import org.jetbrains.exposed.sql.Database

suspend fun main() {
    val DB_URL: String by System.getenv()
    val DB_USER: String by System.getenv()
    val DB_PASSWORD: String by System.getenv()
    val db = Database.connect(
        DB_URL,
        user = DB_USER,
        password = DB_PASSWORD,
        driver = org.postgresql.Driver::class.qualifiedName!!
    )
    populateDatabase(db)
}
