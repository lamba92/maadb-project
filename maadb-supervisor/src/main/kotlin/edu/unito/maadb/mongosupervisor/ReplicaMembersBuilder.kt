package edu.unito.maadb.mongosupervisor

class ReplicaMembersBuilder {

    private val members = mutableListOf<ReplicaMember>()

    fun add(action: ReplicaMember.() -> Unit) =
        ReplicaMember().apply(action).also { members.add(it) }

    fun build() =
        members.toList()
}
