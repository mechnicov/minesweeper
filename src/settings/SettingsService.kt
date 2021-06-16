package com.mines.settings

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.UnprocessableEntityError

interface SettingsService {
    suspend fun create(width: Int, height: Int, bombsCount: Int): Setting
}

class SettingsServiceDB : SettingsService {
    override suspend fun create(width: Int, height: Int, bombsCount: Int): Setting {
        return transaction {
            addLogger(StdOutSqlLogger)

            if (Settings.selectAll().limit(1).firstOrNull() != null ) throw UnprocessableEntityError("Settings exist")

            val id = Settings.insertAndGetId { s ->
                s[Settings.width] = width
                s[Settings.height] = height
                s[Settings.bombsCount] = bombsCount
            }

            Settings.select { Settings.id eq id }.first()
        }.asSetting()
    }

    private fun ResultRow.asSetting() = Setting(
        this[Settings.id].value,
        this[Settings.width],
        this[Settings.height],
        this[Settings.bombsCount],
    )
}
