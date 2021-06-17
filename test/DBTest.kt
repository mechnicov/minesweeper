package com.mines

import com.mines.cells.Cells
import com.mines.games.Games
import com.mines.settings.Settings
import com.mines.users.Users
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DBTest {
    private val dotenv = dotenv { filename = ".env.test" }

    private val host = dotenv["POSTGRES_HOST"]
    private val port = dotenv["POSTGRES_PORT"]
    private val dbName = dotenv["POSTGRES_DB"]
    private val dbUser = dotenv["POSTGRES_USER"]
    private val dbPassword = dotenv["POSTGRES_PASSWORD"]

    fun connect() =
        Database.connect(
            "jdbc:postgresql://$host:$port/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )

    fun prepare() {
        transaction {
            SchemaUtils.create(Games, Settings, Users, Cells)
        }
    }
}
