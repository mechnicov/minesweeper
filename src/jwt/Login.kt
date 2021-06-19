package com.mines.jwt

import io.ktor.auth.*

data class Login(val email: String = "", val password: String = ""): Principal
