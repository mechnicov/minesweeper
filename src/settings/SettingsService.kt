package com.mines.settings

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.mines.UnprocessableEntityError
import io.ktor.features.*

interface SettingsService {
    suspend fun create(width: Int, height: Int, bombsCount: Int): SettingsData
    suspend fun get(): SettingsData
    suspend fun update(width: Int, height: Int, bombsCount: Int): SettingsData
}

class SettingsServiceDB : SettingsService {
    override suspend fun create(width: Int, height: Int, bombsCount: Int): SettingsData {
        return transaction {
            addLogger(StdOutSqlLogger)

            if (Settings.selectAll().limit(1).firstOrNull() != null ) throw UnprocessableEntityError("Settings exist")

            val id = Settings.insertAndGetId { s ->
                s[Settings.width] = width
                s[Settings.height] = height
                s[Settings.bombsCount] = bombsCount
            }

            Settings.select { Settings.id eq id }.first()
        }.asSettingsData()
    }

    override suspend fun get(): SettingsData {
        return transaction {
            addLogger(StdOutSqlLogger)

            Settings.selectAll().limit(1).firstOrNull() ?: throw NotFoundException()
        }.asSettingsData()
    }

    override suspend fun update(width: Int, height: Int, bombsCount: Int): SettingsData {
        return transaction {
            addLogger(StdOutSqlLogger)

            val settingsId = Settings.selectAll().limit(1).firstOrNull()?.asSettingsData()?.id ?: Settings.insertAndGetId {}.toString().toInt()

            Settings.update({ Settings.id eq settingsId }) { s ->
                s[Settings.width] = width
                s[Settings.height] = height
                s[Settings.bombsCount] = bombsCount
            }

            Settings.select { Settings.id eq settingsId }.first()
        }.asSettingsData()
    }

    private fun ResultRow.asSettingsData() = SettingsData(
        this[Settings.id].value,
        this[Settings.width],
        this[Settings.height],
        this[Settings.bombsCount],
    )
}
