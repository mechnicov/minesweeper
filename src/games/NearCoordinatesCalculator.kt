package com.mines.games

object NearCoordinatesCalculator {
    fun nearCoordinates(x: Int, y: Int): ArrayList<ArrayList<Int>> {
        return arrayListOf(
            arrayListOf(x + 1, y),
            arrayListOf(x - 1, y),
            arrayListOf(x, y + 1),
            arrayListOf(x, y - 1),
            arrayListOf(x - 1, y - 1),
            arrayListOf(x - 1, y + 1),
            arrayListOf(x + 1, y - 1),
            arrayListOf(x + 1, y + 1)
        )
    }
}
