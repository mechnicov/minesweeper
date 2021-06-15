package com.mines.settings

import org.jetbrains.exposed.dao.IntIdTable

object Settings : IntIdTable() {
    val width = integer("width").default(10)
    val height = integer("height").default(10)
    val bombsCount = integer("bombs_count").default(5)
}

data class Setting(val id: Int, val width: Int, val height: Int, val bombsCount: Int)
