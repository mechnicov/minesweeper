package com.mines

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import org.slf4j.event.*
import com.fasterxml.jackson.databind.*
import com.mines.api.v1.settingsRouter
import io.ktor.jackson.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.games.Games
import com.mines.settings.Settings
import com.mines.settings.SettingsServiceDB

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
     DB.connect()

    val settingsService = SettingsServiceDB()

    transaction {
        SchemaUtils.create(Games, Settings)
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        settingsRouter(settingsService)

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
