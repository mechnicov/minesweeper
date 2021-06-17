package com.mines.settings

import org.jetbrains.exposed.dao.IntIdTable
import jakarta.validation.constraints.Min
import org.hibernate.validator.constraints.ScriptAssert
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

object Settings : IntIdTable() {
    val width = integer("width").default(10)
    val height = integer("height").default(10)
    val bombsCount = integer("bombs_count").default(5)
}

class Setting(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Setting>(Settings)

    var width by Settings.width
    var height by Settings.height
    var bombsCount by Settings.bombsCount


    fun data(): SettingsData =
        SettingsData(
            this.id.value,
            this.width,
            this.height,
            this.bombsCount,
        )
}

@ScriptAssert(
    lang = "javascript",
    script = "_this.bombsCount < _this.width * _this.height",
    message = "Bombs count must be less than cells count"
)
data class SettingsData(
    val id: Int,
    @field: Min(value = 2)
    val width: Int,
    @field: Min(value = 2)
    val height: Int,
    val bombsCount: Int
)
