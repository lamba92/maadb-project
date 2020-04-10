package edu.unito.maadb.sql.utils

import edu.unito.maadb.core.utils.SpecificSentiment
import edu.unito.maadb.core.utils.TweetsElaborationTools
import edu.unito.maadb.sql.daos.TweetEntity
import edu.unito.maadb.sql.tables.TweetEmojisTable
import edu.unito.maadb.sql.tables.TweetEmoticonsTable
import edu.unito.maadb.sql.tables.TweetHashtagsTable
import edu.unito.maadb.sql.tables.TweetsTable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

@OptIn(ExperimentalStdlibApi::class, FlowPreview::class)
suspend fun populateDatabase(
    database: Database,
    tools: TweetsElaborationTools = TweetsElaborationTools.Defaults
): Map<SpecificSentiment, List<TweetEntity>> {

    newSuspendedTransaction(db = database) {
        SchemaUtils.createMissingTablesAndColumns(
            TweetsTable,
            TweetEmojisTable,
            TweetEmoticonsTable,
            TweetHashtagsTable
        )
    }

    populateResources(database)
    return populateTweets(tools, database)

}

fun populateDatabaseBlocking(
    database: Database,
    tools: TweetsElaborationTools = TweetsElaborationTools.Defaults
) = runBlocking {
    populateDatabase(
        database,
        tools
    )
}

@OptIn(FlowPreview::class)
fun <T, R> Flow<T>.flatMapMergeIterable(
    transform: suspend (T) -> Iterable<R>
): Flow<R> = flatMapMerge { transform(it).asFlow() }
