package com.mines.games

import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
import com.mines.cells.CellStatus
import com.mines.module
import com.mines.mapper
import com.mines.settings.Setting
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Get
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

    @DisplayName("Find Game by ID")
    @Nested
    inner class FindGameById {
        @Test
        fun `when game exists`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    Setting.new {
                        width = 2
                        height = 3
                        bombsCount = 1
                    }
                }

                createGame()

                val response = handleRequest(Get, "/api/v1/games/1").response.content!!
                val game: GameData = mapper.readValue(response)

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

        @Test
        fun `when game does not exist`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                val call = handleRequest(Get, "/api/v1/games/1")

                Assert.assertEquals(HttpStatusCode.NotFound, call.response.status())
                Assert.assertEquals(
                    mapOf("message" to "Resource not found", "errorCode" to 404),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }

    @DisplayName("All Games")
    @Nested
    inner class AllGames {
        @Test
        fun `when games exist`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    Setting.new {
                        width = 2
                        height = 3
                        bombsCount = 1
                    }
                }

                createGame()

                val response = handleRequest(Get, "/api/v1/games").response.content!!
                val games: List<GameData> = mapper.readValue(response)
                val game = games.last()

                Assert.assertEquals(games.size, 1)

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

        @Test
        fun `when games do not exist`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                val call = handleRequest(Get, "/api/v1/games")

                Assert.assertEquals(
                    emptyList<GameData>(),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }

    @DisplayName("Mark Cell")
    @Nested
    inner class MarkCell {
        @Test
        fun `when cell is closed`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    Setting.new {
                        width = 2
                        height = 3
                        bombsCount = 1
                    }
                }

                val game: GameData = mapper.readValue(createGame().response.content!!)
                val gameId = game.id

                val call = markCell(gameId, 0, 2)
                val updatedGame: GameData = mapper.readValue(call.response.content!!)

                Assert.assertEquals(updatedGame.id, gameId)
                Assert.assertEquals(updatedGame.width, 2)
                Assert.assertEquals(updatedGame.height, 3)
                Assert.assertEquals(updatedGame.status, GameStatus.IN_PROGRESS.value)

                Assert.assertEquals(updatedGame.cells.size, 6)
                Assert.assertEquals(updatedGame.cells.find { it.x == 0 && it.y == 2 }?.status, CellStatus.MARKED.value)
                Assert.assertEquals(updatedGame.cells.filter { it.status == CellStatus.CLOSED.value }.size, 5)
            }
        }

        @Test
        fun `when cell is marked`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    Setting.new {
                        width = 2
                        height = 3
                        bombsCount = 1
                    }
                }

                val game: GameData = mapper.readValue(createGame().response.content!!)
                val gameId = game.id

                markCell(gameId, 0, 2)
                val call = markCell(gameId, 0, 2)

                val updatedGame: GameData = mapper.readValue(call.response.content!!)

                Assert.assertEquals(updatedGame.id, gameId)
                Assert.assertEquals(updatedGame.width, 2)
                Assert.assertEquals(updatedGame.height, 3)
                Assert.assertEquals(updatedGame.status, GameStatus.IN_PROGRESS.value)

                Assert.assertEquals(updatedGame.cells.size, 6)
                Assert.assertEquals(updatedGame.cells.filter { it.status == CellStatus.CLOSED.value }.size, 6)
            }
        }
    }
}

fun TestApplicationEngine.createGame(): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/games") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
}

fun TestApplicationEngine.markCell(gameId: Int, x: Int, y: Int): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/games/$gameId/mark") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(mapper.writeValueAsString(mapOf("x" to x, "y" to y)))
    }
}
