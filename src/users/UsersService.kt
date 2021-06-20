package com.mines.users

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.UnprocessableEntityError
import com.mines.jwt.Login
import com.mines.validate
import org.mindrot.jbcrypt.BCrypt

interface UsersService {
    suspend fun create(userData: UserData): UserData
    suspend fun auth(login: Login)
    suspend fun findByEmail(email: String): UserData
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

    override suspend fun auth(login: Login) {
        transaction {
            addLogger(StdOutSqlLogger)

            val user =
                Users.select { Users.email eq login.email }.firstOrNull() ?: throw UnprocessableEntityError("Bad credentials")

            if (!BCrypt.checkpw(login.password, user[Users.password])) throw UnprocessableEntityError("Bad credentials")
        }
    }

    override suspend fun findByEmail(email: String): UserData {
        return transaction {
            addLogger(StdOutSqlLogger)

            val user = User.find { Users.email eq email }.first()

            user.data()
        }
    }
}
