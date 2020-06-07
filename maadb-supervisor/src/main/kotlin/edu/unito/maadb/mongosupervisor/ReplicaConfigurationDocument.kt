package edu.unito.maadb.mongosupervisor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReplicaConfigurationDocument(
    @SerialName("_id") var id: String = "",
    @SerialName("configsvr") var enableConfigurationServer: Boolean = false,
    var members: List<ReplicaMember> = emptyList()
)
