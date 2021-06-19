package com.mines.users

import com.mines.games.Game
import com.mines.games.GameData
import com.mines.games.Games
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Users : IntIdTable() {
    val email = text("email").uniqueIndex()
    val isAdmin = bool("is_admin").default(false)
    val password = text("password")
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var email by Users.email
    var isAdmin by Users.isAdmin
    var password by Users.password
    val games by Game referrersOn Games.user

    fun data(): UserData =
        UserData(
            this.id.value,
            this.email,
            this.isAdmin,
            "[FILTERED]",
            this.games.map { it.data() }.toSet()
        )
}

data class UserData(
    val id: Int,
    val email: String = "",
    val isAdmin: Boolean,
    val password: String = "",
    val games: Set<GameData> = emptySet(),
)
