package com.mines.api.v1

import com.mines.jwt.JWTConfig
import com.mines.jwt.Login
import com.mines.jwt.login
import com.mines.users.UsersService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.authenticationRouter(usersService: UsersService) {
    route("/api/v1/auth") {
        post {
            var login = call.receive<Login>()

            login = usersService.auth(login)

            val token = JWTConfig.generateToken(login)

            call.respond(mapOf("token" to token))
        }

        authenticate {
            get {
                val user = usersService.findByEmail(call.login?.email.toString())
                call.respond(user)
            }
        }
    }
}
