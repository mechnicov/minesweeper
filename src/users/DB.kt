package com.mines.users

import org.jetbrains.exposed.dao.IntIdTable

object Users : IntIdTable() {
    val email = text("email").uniqueIndex()
    val isAdmin = bool("is_admin").default(false)
}

data class User(
    val id: Int,
    val email: String,
    val isAdmin: Boolean
)
