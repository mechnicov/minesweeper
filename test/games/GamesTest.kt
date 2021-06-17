package com.mines.games

import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
import com.mines.cells.CellStatus
import com.mines.module
import com.mines.mapper
import com.mines.settings.Setting
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Games routes")
class GamesTest : ApplicationTest() {
    @DisplayName("Create Game")
    @Nested
    inner class CreateGame {
        @Test
        fun `success`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    Setting.new {
                        width = 2
                        height = 3
                        bombsCount = 1
                    }
                }

                val call = createGame()
                val game: GameData = mapper.readValue(call.response.content!!)

                Assert.assertEquals(HttpStatusCode.OK, call.response.status())

                Assert.assertEquals(game.id, 1)
                Assert.assertEquals(game.width, 2)
                Assert.assertEquals(game.height, 3)
                Assert.assertEquals(game.status, GameStatus.IN_PROGRESS.value)

                Assert.assertEquals(game.cells.size, 6)
                Assert.assertEquals(game.cells.map { it.gameId }.distinct(), listOf(1))
                Assert.assertEquals(game.cells.map { it.status }.distinct(), listOf(CellStatus.CLOSED.value))
                Assert.assertEquals(game.cells.filter { it.isBomb }.size, 1)
                Assertions.assertThat(
                    game.cells.map { it.x to it.y }).hasSameElementsAs(
                    listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 1, 1 to 2)
                )
            }
        }
    }
}

fun TestApplicationEngine.createGame(): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/games") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
}
