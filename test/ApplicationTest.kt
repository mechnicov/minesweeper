package com.mines

import com.mines.cells.Cells
import com.mines.games.Games
import com.mines.settings.Settings
import com.mines.users.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

open class ApplicationTest {
    @BeforeEach
    fun connect() {
        DBTest.connect()
        DBTest.prepare()
    }

    @AfterEach
    fun cleanup() {
        transaction {
            SchemaUtils.drop(Settings, Users, Games, Cells)
        }
    }
}

val mapper = jacksonObjectMapper()
