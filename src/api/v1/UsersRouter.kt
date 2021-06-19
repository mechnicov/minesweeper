package com.mines.api.v1

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
            var user = call.receive<UserData>()
            user.validate()
            user = usersService.create(user.email, user.password)
            call.respond(user)
        }
    }
}
