package com.mines.api.v1

import com.mines.games.GamesServiceDB
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.gamesRouter(gamesService: GamesServiceDB) {
    route("/api/v1/games") {
        post {
            val game = gamesService.create()
            call.respond(game)
        }

        get("/{id}") {
            val id =
                try {
                    requireNotNull(call.parameters["id"]).toInt()
                } catch (e: NumberFormatException) {
                    throw NotFoundException()
                }

            val game = gamesService.findById(id)
            call.respond(game)
        }

        get {
            call.respond(gamesService.all())
        }
    }
}
