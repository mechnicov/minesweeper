package com.mines.users

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Users : IntIdTable() {
    val email = text("email").uniqueIndex()
    val isAdmin = bool("is_admin").default(false)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var email by Users.email
    var isAdmin by Users.isAdmin

    fun data(): UserData =
        UserData(
            this.id.value,
            this.email,
            this.isAdmin,
        )
}

data class UserData(
    val id: Int,
    val email: String,
    val isAdmin: Boolean
)
