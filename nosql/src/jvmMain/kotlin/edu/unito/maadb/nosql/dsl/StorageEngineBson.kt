package edu.unito.maadb.nosql.dsl

import kotlinx.serialization.Serializable

@Serializable
data class StorageEngineBson(
    var autoIndexId: Boolean = true
)
