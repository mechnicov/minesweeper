package com.mines.games

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import com.mines.cells.Cell
import com.mines.cells.CellData
import com.mines.cells.Cells
import com.mines.users.User
import com.mines.users.Users
import org.jetbrains.exposed.sql.CurrentDateTime
import org.jetbrains.exposed.sql.ReferenceOption

object Games : IntIdTable() {
    val width = integer("width")
    val height = integer("height")
    val status = enumerationByName("status", 20, GameStatus::class).default(GameStatus.IN_PROGRESS)
    val bombsCount = integer("bombs_count")
    val openingsCount = integer("openings_count").default(0)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val user = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
}

class Game(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Game>(Games)

    var width by Games.width
    var height by Games.height
    var status by Games.status
    var bombsCount by Games.bombsCount
    var openingsCount by Games.openingsCount
    var createdAt by Games.createdAt
    var user by User referencedOn Games.user
    val cells by Cell referrersOn Cells.game

    fun data(): GameData {
        return GameData(
            this.id.value,
            this.width,
            this.height,
            this.status.value,
            this.bombsCount,
            this.openingsCount,
            this.createdAt.toDateTimeISO().toString(),
            this.user.id.value,
            this.cells.map { it.data() }
        )
    }
}

data class GameData(
    val id: Int,
    val width: Int,
    val height: Int,
    val status: String,
    val bombsCount: Int,
    val openingsCount: Int,
    val createdAt: String,
    val userId: Int,
    var cells: List<CellData>
) {
    init {
       cells = cells.sortedWith(compareBy(CellData::y, CellData::x))
    }
}

enum class GameStatus(val value: String) {
    IN_PROGRESS("in_progress"),
    FAIL("fail"),
    WON("won"),
}
