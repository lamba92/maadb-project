package edu.unito.maadb.sql.tests

import edu.unito.maadb.sql.populateDatabase
import kotlinx.coroutines.FlowPreview
import org.jetbrains.exposed.sql.Database
import org.postgresql.Driver

@FlowPreview
@ExperimentalStdlibApi
suspend fun main() {
    val db = Database.connect(
        url = "jdbc:postgresql://192.168.1.158/maadb",
        driver = Driver::class.qualifiedName!!,
        user = "postgres",
        password = "postgres"
    )
    populateDatabase(db)
}
