package com.mines.cells

import com.mines.games.Game
import com.mines.games.Games
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object Cells : IntIdTable() {
    val x = integer("x")
    val y = integer("y")
    val isBomb = bool("is_bomb").default(false)
    val status = enumerationByName("status", 20, CellStatus::class).default(CellStatus.CLOSED)
    val game = reference("game_id", Games)
}

class Cell(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Cell>(Cells)

    var x by Cells.x
    var y by Cells.y
    var isBomb by Cells.isBomb
    var status by Cells.status
    var game by Game referencedOn Cells.game

    fun data(): CellData =
        CellData(
            this.id.value,
            this.x,
            this.y,
            this.isBomb,
            this.status.value,
            this.game.id.value,
        )
}

data class CellData(
    val id: Int,
    val x: Int,
    val y: Int,
    val isBomb: Boolean,
    val status: String,
    val gameId: Int,
)

enum class CellStatus(val value: String) {
    MARKED("marked"),
    CLOSED("closed"),
    EMPTY("empty"),
}
