package com.mines.settings

import org.jetbrains.exposed.dao.IntIdTable
import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.ScriptAssert

object Settings : IntIdTable() {
    val width = integer("width").default(10)
    val height = integer("height").default(10)
    val bombsCount = integer("bombs_count").default(5)
}

@ScriptAssert(
    lang = "javascript",
    script = "_this.bombsCount < _this.width * _this.height",
    message = "Bombs count must be less than cells count"
)
data class Setting(
    val id: Int,
    @field: Min(value = 2)
    val width: Int,
    @field: Min(value = 2)
    val height: Int,
    val bombsCount: Int
)
