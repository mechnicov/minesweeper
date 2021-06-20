package com.mines.jwt

import com.mines.ForbiddenError
import io.ktor.application.*
import io.ktor.auth.*

data class Login(val email: String = "", val password: String = "", var isAdmin: Boolean = false): Principal

val ApplicationCall.login get() = authentication.principal<Login>()

fun ApplicationCall.checkIsAdmin() {
    if (this.login?.isAdmin == false) throw ForbiddenError()
}
