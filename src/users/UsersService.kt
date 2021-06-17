package com.mines.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.UnprocessableEntityError

interface UsersService {
    suspend fun create(email: String): UserData
}

class UsersServiceDB : UsersService {
    override suspend fun create(emailParam: String): UserData {
        return transaction {
            addLogger(StdOutSqlLogger)

            if (Users.select { Users.email eq emailParam }.firstOrNull() != null) throw UnprocessableEntityError("User exists")

            val newUser = User.new {
                email = emailParam
            }

            newUser.data()
        }
    }
}
