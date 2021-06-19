package com.mines.api.v1

import com.mines.jwt.JWTConfig
import com.mines.jwt.Login
import com.mines.users.UsersService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.authenticationRouter(usersService: UsersService) {
    route("/api/v1/auth") {
        post {
            val login = call.receive<Login>()

            usersService.auth(login)

            val token = JWTConfig.generateToken(login)

            call.respond(mapOf("token" to token))
        }
    }
}
