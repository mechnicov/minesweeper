package com.mines.games

import com.mines.cells.Cell
import com.mines.cells.CellStatus.*
import com.mines.games.GameStatus.*

object CellMarker {
    fun tryToMark(cell: Cell) {
        val game = cell.game

        if (game.status != IN_PROGRESS || cell.status == EMPTY) return

        when (cell.status) {
            CLOSED -> cell.status = MARKED
            MARKED -> cell.status = QUESTION
            QUESTION -> cell.status = CLOSED
        }
    }
}
