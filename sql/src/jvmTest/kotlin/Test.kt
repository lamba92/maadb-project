import edu.unito.maadb.sql.utils.populateDatabase
import org.jetbrains.exposed.sql.Database

@OptIn(ExperimentalStdlibApi::class, kotlinx.coroutines.FlowPreview::class)
suspend fun main() {
    val db = Database.connect(
        url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://192.168.1.159:5432/",
        user = System.getenv("DATABASE_USER") ?: "postgres",
        password = System.getenv("DATABASE_PASSWORD") ?: "mysecretpassword",
        driver = org.postgresql.Driver::class.qualifiedName!!
    )
    populateDatabase(db)
}
