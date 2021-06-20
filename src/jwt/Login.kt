package com.mines.jwt

import io.ktor.application.*
import io.ktor.auth.*

data class Login(val email: String = "", val password: String = ""): Principal

val ApplicationCall.login get() = authentication.principal<Login>()
