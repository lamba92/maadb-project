package edu.unito.maadb.nosql.dsl

import kotlinx.serialization.Serializable

@Serializable
data class StorageEngineSettings(
    var autoIndexId: Boolean = true
)
