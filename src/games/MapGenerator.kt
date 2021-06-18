package com.mines.games

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

    private fun bombsNear(x: Int, y: Int, locations: MutableList<ArrayList<Int>>): Int {
        return arrayListOf(
            arrayListOf(x + 1, y),
            arrayListOf(x - 1, y),
            arrayListOf(x, y + 1),
            arrayListOf(x, y - 1),
            arrayListOf(x - 1, y - 1),
            arrayListOf(x - 1, y + 1),
            arrayListOf(x + 1, y - 1),
            arrayListOf(x + 1, y + 1)
        ).count { locations.contains(it) }
    }
}
