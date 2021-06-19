package com.mines.games

import com.mines.cells.Cell
import com.mines.cells.CellStatus.*
import com.mines.games.GameStatus.*

class CellOpener(private val cell: Cell) {
    private val game = cell.game

    fun tryToOpen() {
        if (cell.status != CLOSED) return

        if (cell.isBomb) return finishFail()

        open(cell)
    }

    private fun open(cell: Cell) {
        if (game.status != IN_PROGRESS) return

        cell.status = EMPTY

        if (game.cells.asSequence().filter { !it.isBomb }.all { it.status == EMPTY }) return finishWin()

        if (cell.bombsNear == 0) {
            NearCoordinatesCalculator.
                nearCoordinates(cell.x, cell.y).
                forEach { coordinates ->
                    val nearCell =
                        game.cells.find { it.x == coordinates.first() && it.y == coordinates.last() } ?: return@forEach

                    if (nearCell.status == CLOSED && !nearCell.isBomb) open(nearCell)
                }
        }
    }

    private fun finishFail() {
        game.status = FAIL
    }

    private fun finishWin() {
        game.status = WON
        game.cells.filter { it.isBomb }.forEach { it.status = MARKED }
    }
}
