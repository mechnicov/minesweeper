package com.mines.api.v1

import com.mines.jwt.JWTConfig
import com.mines.jwt.Login
import com.mines.users.UserData
import com.mines.users.UsersService
import com.mines.validate
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.usersRouter(usersService: UsersService) {
    route("/api/v1/users") {
        post {
            val userData = call.receive<UserData>()

            usersService.create(userData)

            val token = JWTConfig.generateToken(Login(userData.email, userData.password))

            call.respond(mapOf("token" to token))
        }
    }
}
