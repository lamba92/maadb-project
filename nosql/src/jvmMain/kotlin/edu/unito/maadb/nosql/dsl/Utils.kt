package edu.unito.maadb.nosql.dsl

import com.github.jershell.kbson.*
import com.mongodb.client.model.CreateCollectionOptions
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.bson.Document
import org.bson.conversions.Bson
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
        serializer: Json = Json(JsonConfiguration.Stable.copy(prettyPrint = true)),
        action: StorageEngineSettings.() -> Unit
): CreateCollectionOptions =
    storageEngineOptions(
        Document.parse(serializer.stringify(
            StorageEngineSettings.serializer(),
            StorageEngineSettings().apply(action)
        ))
    )
