package edu.unito.maadb.nosql.dsl

import com.github.jershell.kbson.KBson
import com.mongodb.client.model.CreateCollectionOptions
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

suspend inline fun <reified T : Any> CoroutineDatabase.createCollection(
    collectionName: String,
    options: CreateCollectionOptions.() -> Unit = {}
): CoroutineCollection<T> {
    createCollection(collectionName, CreateCollectionOptions().apply(options))
    return getCollection(collectionName)
}

fun CreateCollectionOptions.storageEngineOptions(
    serializer: KBson = KBson(),
    action: StorageEngineSettings.() -> Unit
): CreateCollectionOptions =
    storageEngineOptions(
        serializer.stringify(
            StorageEngineSettings.serializer(),
            StorageEngineSettings().apply(action)
        )
    )
