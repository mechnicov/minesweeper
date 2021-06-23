package com.mines.games

import com.mines.cells.Cell
import com.mines.cells.CellStatus.*
import com.mines.games.GameStatus.*
import com.mines.games.NearCoordinatesCalculator.nearCoordinates

class CellOpener(private val cell: Cell) {
    private val game = cell.game

    fun tryToOpen() {
        if (cell.status != CLOSED) return

        if (cell.isBomb) {
            if (game.openingsCount > 0) return finishFail()
            return rescue()
        }

        open(cell)
    }

    private fun open(cell: Cell, auto: Boolean = false) {
        if (game.status != IN_PROGRESS) return

        if (!auto) game.openingsCount++

        cell.status = EMPTY

        if (game.cells.asSequence().filter { !it.isBomb }.all { it.status == EMPTY }) return finishWin()

        if (cell.bombsNear == 0) {
            nearCoordinates(cell.x, cell.y).forEach { coordinates ->
                val nearCell =
                    game.cells.find { it.x == coordinates.first() && it.y == coordinates.last() } ?: return@forEach

                if (nearCell.status == CLOSED && !nearCell.isBomb) open(nearCell, true)
            }
        }
    }

    private fun rescue() {
        BombCellRescuer(cell).rescue()

        open(cell)
    }

    private fun finishFail() {
        game.status = FAIL
        game.cells.filter { it.isBomb }.forEach { it.status = BOMB }
        cell.status = EXPLODED
    }

    private fun finishWin() {
        game.status = WON
        game.cells.filter { it.isBomb }.forEach { it.status = MARKED }
    }
}
