package com.mines.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.UnprocessableEntityError
import com.mines.validate
import org.mindrot.jbcrypt.BCrypt

interface UsersService {
    suspend fun create(userData: UserData): UserData
}

class UsersServiceDB : UsersService {
    override suspend fun create(userData: UserData): UserData {
        return transaction {
            addLogger(StdOutSqlLogger)

            userData.validate()

            if (Users.select { Users.email eq userData.email }.firstOrNull() != null) throw UnprocessableEntityError("User exists")

            val newUser = User.new {
                email = userData.email
                password = BCrypt.hashpw(userData.password, BCrypt.gensalt())
            }

            newUser.data()
        }
    }
}
