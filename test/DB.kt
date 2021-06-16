package com.mines

import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

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
}
