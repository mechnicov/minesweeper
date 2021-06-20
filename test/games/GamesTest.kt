package com.mines.games

import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
import com.mines.cells.CellStatus
import com.mines.module
import com.mines.mapper
import com.mines.settings.Setting
import com.mines.users.User
import com.mines.users.authUser
import com.mines.users.createUser
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt

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

                    User.new {
                        email = "user@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                    }
                }

                val authCall = authUser("user@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = createGame(token)
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

                    User.new {
                        email = "user@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                    }
                }

                val authCall = authUser("user@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                createGame(token)

                val response = handleRequest(Get, "/api/v1/games/1") {
                    addHeader(HttpHeaders.Authorization, "Bearer $token")
                }.response.content!!
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
                transaction {
                    Setting.new {
                        width = 2
                        height = 3
                        bombsCount = 1
                    }

                    User.new {
                        email = "user@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                    }
                }

                val authCall = authUser("user@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = handleRequest(Get, "/api/v1/games/1") {
                    addHeader(HttpHeaders.Authorization, "Bearer $token")
                }

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

                    User.new {
                        email = "user@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                    }
                }

                val authCall = authUser("user@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                createGame(token)

                val response = handleRequest(Get, "/api/v1/games"){
                    addHeader(HttpHeaders.Authorization, "Bearer $token")
                }.response.content!!
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
                val createCall = createUser("user@example.com", "qwerty")
                val createResponse: Map<String, String> = mapper.readValue(createCall.response.content!!)

                val token = createResponse["token"].toString()

                val call = handleRequest(Get, "/api/v1/games") {
                    addHeader(HttpHeaders.Authorization, "Bearer $token")
                }

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
        fun `when user is not owner`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    Setting.new {
                        width = 2
                        height = 3
                        bombsCount = 1
                    }
                }

                val createOwner = createUser("user@example.com", "qwerty")
                val createOwnerResponse: Map<String, String> = mapper.readValue(createOwner.response.content!!)

                val ownerToken = createOwnerResponse["token"].toString()

                val game: GameData = mapper.readValue(createGame(ownerToken).response.content!!)
                val gameId = game.id

                val otherUser = createUser("other@example.com", "qwerty")
                val otherUserResponse: Map<String, String> = mapper.readValue(otherUser.response.content!!)

                val otherUserToken = otherUserResponse["token"].toString()

                val call = markCell(gameId, 0, 2, otherUserToken)

                Assert.assertEquals(HttpStatusCode.NotFound, call.response.status())
                Assert.assertEquals(
                    mapOf("message" to "Resource not found", "errorCode" to 404),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

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

                val createCall = createUser("user@example.com", "qwerty")
                val createResponse: Map<String, String> = mapper.readValue(createCall.response.content!!)

                val token = createResponse["token"].toString()

                val game: GameData = mapper.readValue(createGame(token).response.content!!)
                val gameId = game.id

                val call = markCell(gameId, 0, 2, token)
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

                val createCall = createUser("user@example.com", "qwerty")
                val createResponse: Map<String, String> = mapper.readValue(createCall.response.content!!)

                val token = createResponse["token"].toString()

                val game: GameData = mapper.readValue(createGame(token).response.content!!)
                val gameId = game.id

                markCell(gameId, 0, 2, token)
                val call = markCell(gameId, 0, 2, token)

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

fun TestApplicationEngine.createGame(token: String): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/games") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Authorization, "Bearer $token")
    }
}

fun TestApplicationEngine.markCell(gameId: Int, x: Int, y: Int, token: String): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/games/$gameId/mark") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Authorization, "Bearer $token")
        setBody(mapper.writeValueAsString(mapOf("x" to x, "y" to y)))
    }
}
