package edu.unito.maadb.mongosupervisor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReplicaMember(
    @SerialName("_id") var id: Int = -1,
    var host: String = "localhost:27019"
)
