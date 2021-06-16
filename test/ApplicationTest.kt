package com.mines

import com.mines.settings.Settings
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

open class ApplicationTest {
    @BeforeEach
    fun connect() {
        DBTest.connect()
    }

    @AfterEach
    fun cleanup() {
        transaction {
            SchemaUtils.drop(Settings)
        }
    }
}
