package com.mines.jwt

import com.mines.ForbiddenError
import com.mines.users.User
import com.mines.users.Users
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import org.jetbrains.exposed.sql.transactions.transaction

data class Login(val email: String = "", val password: String = "", var isAdmin: Boolean = false): Principal

val ApplicationCall.login get() = authentication.principal<Login>()

fun ApplicationCall.checkIsAdmin() {
    if (this.login?.isAdmin == false) throw ForbiddenError()
}

fun ApplicationCall.checkIsGameOwner(id: Int) {
    val userEmail = this.login?.email.toString()
    if (transaction {
            User.find { Users.email eq userEmail }.first().games.none { it.id.value == id }
        }
    ) throw NotFoundException()
}
