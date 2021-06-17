package com.mines.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.UnprocessableEntityError

interface UsersService {
    suspend fun create(email: String): UserData
}

class UsersServiceDB : UsersService {
    override suspend fun create(email: String): UserData {
        return transaction {
            addLogger(StdOutSqlLogger)

            if (Users.select { Users.email eq email }.firstOrNull() != null) throw UnprocessableEntityError("User exists")

            val id = Users.insertAndGetId { s -> s[Users.email] = email }

            Users.select { Users.id eq id }.first()
        }.asUser()
    }

    private fun ResultRow.asUser() = UserData(
        this[Users.id].value,
        this[Users.email],
        this[Users.isAdmin],
    )
}
