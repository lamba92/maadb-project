import edu.unito.maadb.nosql.utils.populateTweets
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

suspend fun main() {

    val db = KMongo.createClient("mongodb://192.168.1.158")
        .getDatabase("maadb")
        .coroutine

    populateTweets(db)
}
