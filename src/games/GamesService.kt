package com.mines.games

import com.mines.cells.Cell
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.settings.Setting
import com.mines.users.User
import io.ktor.features.*
import java.util.*

interface GamesService {
    suspend fun create(): GameData
    suspend fun findById(id: Int): GameData
    suspend fun all(): List<GameData>
    suspend fun markCell(gameId: Int, x: Int, y: Int): GameData
    suspend fun openCell(gameId: Int, x: Int, y: Int): GameData
}

class GamesServiceDB : GamesService {
    override suspend fun create(): GameData {
        return transaction {
            addLogger(StdOutSqlLogger)

            val settings = Setting.all().limit(1).first()

            val gameUser = User.new { email = "${UUID.randomUUID()}@example.com" } // TODO: authenticated user

            val newGame = Game.new {
                width = settings.width
                height = settings.height
                user = gameUser
            }

            val map = MapGenerator(settings.width, settings.height, settings.bombsCount).generateMap()
            CellsGenerator.generateCellsForGame(newGame, map)

            newGame.data()
        }
    }

    override suspend fun findById(id: Int): GameData {
        return transaction {
            addLogger(StdOutSqlLogger)

            val game = Game.findById(id)

            game?.data() ?: throw NotFoundException()
        }
    }

    override suspend fun all(): List<GameData> {
        return transaction {
            Game.all().map { it.data() }
        }
    }

    override suspend fun markCell(gameId: Int, x: Int, y: Int): GameData {
        return transaction {
            addLogger(StdOutSqlLogger)

            val cell = findCellByGameIdAndCoordinates(gameId, x, y)

            CellMarker.tryToMark(cell)

            cell.game.data()
        }
    }

    override suspend fun openCell(gameId: Int, x: Int, y: Int): GameData {
        return transaction {
            addLogger(StdOutSqlLogger)

            val cell = findCellByGameIdAndCoordinates(gameId, x, y)

            CellOpener(cell).tryToOpen()

            cell.game.data()
        }
    }

    private fun findCellByGameIdAndCoordinates(gameId: Int, x: Int, y: Int): Cell {
        return Game.findById(gameId)?.cells?.find { it.x == x && it.y == y } ?: throw NotFoundException()
    }
}
