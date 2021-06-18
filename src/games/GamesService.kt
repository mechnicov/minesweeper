package com.mines.games

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.cells.Cells
import com.mines.cells.CellStatus
import com.mines.settings.Setting
import com.mines.users.User
import io.ktor.features.*
import java.util.*

interface GamesService {
    suspend fun create(): GameData
    suspend fun findById(id: Int): GameData
    suspend fun all(): List<GameData>
    suspend fun markCell(gameId: Int, x: Int, y: Int): GameData
}

class GamesServiceDB : GamesService {
    override suspend fun create(): GameData {
        return transaction {
            addLogger(StdOutSqlLogger)

            val settings = Setting.all().limit(1).first()

            val settingsWidth = settings.width
            val settingsHeight = settings.height
            val settingsBombsCount = settings.bombsCount

            val gameUser = User.new { email = "${UUID.randomUUID()}@example.com" } // TODO: authenticated user

            val newGame = Game.new {
                width = settingsWidth
                height = settingsHeight
                user = gameUser
            }

            val map = MapGenerator(settingsWidth, settingsHeight, settingsBombsCount).generateMap()

            Cells.batchInsert(map) { cell ->
                this[Cells.x] = cell.x
                this[Cells.y] = cell.y
                this[Cells.isBomb] = cell.isBomb
                this[Cells.bombsNear] = cell.bombsNear
                this[Cells.game] = newGame.id
            }

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

            val game = Game.findById(gameId)

            game ?: throw NotFoundException()

            val cell = game.cells.find { it.x == x && it.y == y }

            cell ?: throw NotFoundException()

            when (cell.status) {
                CellStatus.CLOSED -> cell.status = CellStatus.MARKED
                CellStatus.MARKED -> cell.status = CellStatus.CLOSED
            }

            game.data()
        }
    }
}
