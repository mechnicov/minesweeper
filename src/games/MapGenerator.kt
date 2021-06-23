package com.mines.games

import com.mines.games.NearCoordinatesCalculator.bombsNear

class MapGenerator(private val width: Int, private val height: Int, private val bombsCount: Int) {
    data class MapCell(val x: Int, val y: Int, val isBomb: Boolean = false, val bombsNear: Int)

    private val bombsLocations = mutableListOf<ArrayList<Int>>()
    private val map = mutableListOf<MapCell>()

    fun generateMap(): MutableList<MapCell> {
        installBombs()

        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                map.add(
                    MapCell(
                        x,
                        y,
                        bombsLocations.contains(arrayListOf(x, y)),
                        bombsNear(x, y, bombsLocations)
                    )
                )
            }
        }

        return map
    }

    private fun installBombs() {
        (0 until bombsCount).forEach {
            install@ while (true) {
                val position = arrayListOf((0 until width).random(), (0 until height).random())

                if (bombsLocations.contains(position)) continue

                bombsLocations.add(position)
                break@install
            }
        }
    }
}
