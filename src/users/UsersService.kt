package com.mines.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.UnprocessableEntityError
import org.mindrot.jbcrypt.BCrypt

interface UsersService {
    suspend fun create(email: String, password: String): UserData
}

class UsersServiceDB : UsersService {
    override suspend fun create(emailParam: String, passwordParam: String): UserData {
        return transaction {
            addLogger(StdOutSqlLogger)

            if (Users.select { Users.email eq emailParam }.firstOrNull() != null) throw UnprocessableEntityError("User exists")

            val newUser = User.new {
                email = emailParam
                password = BCrypt.hashpw(passwordParam, BCrypt.gensalt())
            }

            newUser.data()
        }
    }
}
