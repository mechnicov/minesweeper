package com.mines.api.v1

import com.mines.games.GamesServiceDB
import com.mines.jwt.checkIsGameOwner
import com.mines.jwt.login
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.gamesRouter(gamesService: GamesServiceDB) {
    route("/api/v1/games") {
        post {
            val game = gamesService.create(userEmail = call.login?.email.toString())
            call.respond(game)
        }

        get {
            call.respond(gamesService.all(userEmail = call.login?.email.toString()))
        }

        get("/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()

            call.checkIsGameOwner(id)

            val game = gamesService.findById(id)
            call.respond(game)
        }

        post("/{id}/mark") {
            val id = requireNotNull(call.parameters["id"]).toInt()

            call.checkIsGameOwner(id)

            val coordinates = call.receive<CellCoordinate>()

            val game = gamesService.markCell(id, coordinates.x, coordinates.y)

            call.respond(game)
        }

        post("/{id}/open") {
            val id = requireNotNull(call.parameters["id"]).toInt()

            call.checkIsGameOwner(id)

            val coordinates = call.receive<CellCoordinate>()

            val game = gamesService.openCell(id, coordinates.x, coordinates.y)

            call.respond(game)
        }
    }
}

data class CellCoordinate(val x: Int, val y: Int)
