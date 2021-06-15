package com.mines.games

import org.jetbrains.exposed.dao.IntIdTable

object Games : IntIdTable() {
    val width = integer("width")
    val height = integer("height")
    val status = enumerationByName("status", 20, GameStatus::class)
}

data class Game(val id: Int, val width: Int, val height: Int, val status: String)

enum class GameStatus(val value: String) {
    IN_PROGRESS("in_progress"),
    FAIL("fail"),
    WON("won"),
}
