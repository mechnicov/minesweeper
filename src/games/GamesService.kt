package com.mines.games

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.cells.Cell
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

            val map = BombInstaller.bombsMap(settingsWidth, settingsHeight, settingsBombsCount)

            for (i in 0 until settingsWidth) {
                for (j in 0 until settingsHeight) {
                    Cell.new {
                        game = newGame
                        x = i
                        y = j
                        isBomb = map[i][j]
                    }
                }
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

object BombInstaller {
    fun bombsMap(width: Int, height: Int, bombsCount: Int): Array<Array<Boolean>> {
        val map = Array(width) { Array(height) { false } }
        var installedBombsCount = 0

        install@ while (true) {
            for (i in 0 until width) {
                for (j in 0 until height) {
                    if (!map[i][j] && arrayOf(true, false).random()) {
                        installedBombsCount++
                        map[i][j] = true

                        if (installedBombsCount >= bombsCount) break@install
                    }
                }
            }
        }

        return map
    }
}
