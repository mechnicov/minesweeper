package com.mines

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import org.slf4j.event.*
import com.fasterxml.jackson.databind.*
import com.mines.api.v1.authenticationRouter
import com.mines.api.v1.gamesRouter
import com.mines.api.v1.settingsRouter
import com.mines.api.v1.usersRouter
import com.mines.games.GamesServiceDB
import com.mines.jwt.JWTConfig
import com.mines.jwt.Login
import io.ktor.jackson.*
import com.mines.settings.SettingsServiceDB
import com.mines.users.UsersServiceDB
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.github.cdimascio.dotenv.dotenv

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
     if (!testing) {
         DB.connect()
         DB.prepare()
     }

    val usersService = UsersServiceDB()

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(Authentication) {
        jwt {
            realm = dotenv["JWT_REALM"]

            verifier(JWTConfig.verifier)

            validate {
                val name = it.payload.getClaim("email").asString()
                val password = it.payload.getClaim("password").asString()

                if (name != null && password != null) {
                    Login(name, password)
                } else {
                    null
                }
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        gamesRouter(GamesServiceDB())
        settingsRouter(SettingsServiceDB())
        usersRouter(usersService)
        authenticationRouter(usersService)

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }
            exception<NotFoundException> {
                call.respond(HttpStatusCode.NotFound, ErrorMessage("Resource not found", HttpStatusCode.NotFound.value))
            }
            exception<UnprocessableEntityError> { e ->
                call.respond(HttpStatusCode.UnprocessableEntity, ErrorMessage(e.message.toString(), HttpStatusCode.UnprocessableEntity.value))
            }
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
data class ErrorMessage(val message: String, val errorCode: Int)

val dotenv = dotenv()
