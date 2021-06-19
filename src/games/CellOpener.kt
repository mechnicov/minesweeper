package com.mines.games

import com.mines.cells.Cell
import com.mines.cells.CellStatus

object CellOpener {
    fun tryToOpen(cell: Cell) {
        val game = cell.game

        if (game.status != GameStatus.IN_PROGRESS || cell.status != CellStatus.CLOSED) return

        if (cell.isBomb) return finish(game)

        open(cell)
    }

    private fun open(cell: Cell) {
        cell.status = CellStatus.EMPTY

        if (cell.bombsNear == 0) {
            NearCoordinatesCalculator.
                nearCoordinates(cell.x, cell.y).
                forEach { coordinates ->
                    val nearCell =
                        cell.game.cells.find { it.x == coordinates.first() && it.y == coordinates.last() } ?: return@forEach

                    if (nearCell.status == CellStatus.CLOSED && !nearCell.isBomb) open(nearCell)
                }
        }
    }

    private fun finish(game: Game) {
        game.status = GameStatus.FAIL

        game.cells.forEach { it.status = CellStatus.CLOSED }
    }
}
