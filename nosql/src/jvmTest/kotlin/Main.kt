import edu.unito.maadb.nosql.utils.populateTweets
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main() {

    val db = KMongo.createClient("mongodb://192.168.1.158")
            .getDatabase("maadb")
            .coroutine

    runBlocking {
        populateTweets(db)
    }
}
