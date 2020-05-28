package edu.unito.maadb.sql.tests

import edu.unito.maadb.sql.utils.populateDatabase
import kotlinx.coroutines.FlowPreview
import org.jetbrains.exposed.sql.Database
import org.postgresql.Driver

@FlowPreview
@ExperimentalStdlibApi
suspend fun main() {
  val db =
      Database.connect(
          "jdbc:postgresql://192.168.1.158:5432/",
          user = "postgres",
          password = "postgres"
      )
  populateDatabase(db)
}
