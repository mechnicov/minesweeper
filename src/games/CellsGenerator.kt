package com.mines.games

import com.mines.cells.Cells
import org.jetbrains.exposed.sql.batchInsert

object CellsGenerator {
    fun generateCellsForGame(game: Game, map: MutableList<MapGenerator.MapCell>) {
        Cells.batchInsert(map) { cell ->
            this[Cells.x] = cell.x
            this[Cells.y] = cell.y
            this[Cells.isBomb] = cell.isBomb
            this[Cells.bombsNear] = cell.bombsNear
            this[Cells.game] = game.id
        }
    }
}
