package com.mines.api.v1

import com.mines.users.User
import com.mines.users.UsersService
import com.mines.validate
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import jakarta.validation.Validation

fun Route.usersRouter(usersService: UsersService) {
    route("/api/v1/users") {
        val validator = Validation.buildDefaultValidatorFactory().validator

        post {
            var user = call.receive<User>()
            user.validate(validator)
            user = usersService.create(user.email)
            call.respond(user)
        }
    }
}
