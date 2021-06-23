package com.mines.games

import com.mines.cells.Cell

class BombCellRescuer(val cell: Cell) {
    private val game = cell.game
    private val randomNotBombCell = game.cells.first { !it.isBomb }
    private val coordinatesToBeUpdated = mutableSetOf<ArrayList<Int>>()
    private lateinit var bombsLocations: MutableList<ArrayList<Int>>

    fun rescue() {
        swapBombAndNotBomb()
        findAllBombs()
        findCoordinatesToBeUpdated()
        updateCells()
    }

    private fun swapBombAndNotBomb() {
        cell.isBomb = false
        randomNotBombCell.isBomb = true
    }

    private fun findAllBombs() {
        bombsLocations = game.cells.asSequence().filter { it.isBomb }.map { arrayListOf(it.x, it.y) }.toMutableList()
    }

    private fun findCoordinatesToBeUpdated() {
        coordinatesToBeUpdated.add(arrayListOf(cell.x, cell.y))
        coordinatesToBeUpdated.addAll(NearCoordinatesCalculator.nearCoordinates(cell.x, cell.y))

        coordinatesToBeUpdated.add(arrayListOf(randomNotBombCell.x, randomNotBombCell.y))
        coordinatesToBeUpdated.addAll(NearCoordinatesCalculator.nearCoordinates(randomNotBombCell.x, randomNotBombCell.y))
    }

    private fun updateCells() {
        coordinatesToBeUpdated.forEach { coordinates ->
            val c = game.cells.firstOrNull { it.x == coordinates.first() && it.y == coordinates.last() } ?: return@forEach

            c.bombsNear = NearCoordinatesCalculator.bombsNear(c.x, c.y, bombsLocations)
        }
    }
}
