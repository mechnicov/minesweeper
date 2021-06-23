package com.mines.games

import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
import com.mines.cells.Cell
import com.mines.cells.CellStatus
import com.mines.cells.Cells
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
import org.junit.Assert.assertEquals
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

                assertEquals(HttpStatusCode.OK, call.response.status())

                assertEquals(game.id, 1)
                assertEquals(game.width, 2)
                assertEquals(game.height, 3)
                assertEquals(game.status, GameStatus.IN_PROGRESS.value)

                assertEquals(game.cells.size, 6)
                assertEquals(game.cells.map { it.gameId }.distinct(), listOf(1))
                assertEquals(game.cells.map { it.status }.distinct(), listOf(CellStatus.CLOSED.value))
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

                assertEquals(game.id, 1)
                assertEquals(game.width, 2)
                assertEquals(game.height, 3)
                assertEquals(game.status, GameStatus.IN_PROGRESS.value)

                assertEquals(game.cells.size, 6)
                assertEquals(game.cells.map { it.gameId }.distinct(), listOf(1))
                assertEquals(game.cells.map { it.status }.distinct(), listOf(CellStatus.CLOSED.value))
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

                assertEquals(HttpStatusCode.NotFound, call.response.status())
                assertEquals(
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

                assertEquals(games.size, 1)

                assertEquals(game.id, 1)
                assertEquals(game.width, 2)
                assertEquals(game.height, 3)
                assertEquals(game.status, GameStatus.IN_PROGRESS.value)

                assertEquals(game.cells.size, 6)
                assertEquals(game.cells.map { it.gameId }.distinct(), listOf(1))
                assertEquals(game.cells.map { it.status }.distinct(), listOf(CellStatus.CLOSED.value))
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

                assertEquals(
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

                assertEquals(HttpStatusCode.NotFound, call.response.status())
                assertEquals(
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

                assertEquals(updatedGame.id, gameId)
                assertEquals(updatedGame.width, 2)
                assertEquals(updatedGame.height, 3)
                assertEquals(updatedGame.status, GameStatus.IN_PROGRESS.value)

                assertEquals(updatedGame.cells.size, 6)
                assertEquals(updatedGame.cells.find { it.x == 0 && it.y == 2 }?.status, CellStatus.MARKED.value)
                assertEquals(updatedGame.cells.filter { it.status == CellStatus.CLOSED.value }.size, 5)
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

                assertEquals(updatedGame.id, gameId)
                assertEquals(updatedGame.width, 2)
                assertEquals(updatedGame.height, 3)
                assertEquals(updatedGame.status, GameStatus.IN_PROGRESS.value)

                assertEquals(updatedGame.cells.size, 6)
                assertEquals(updatedGame.cells.find { it.x == 0 && it.y == 2 }?.status, CellStatus.QUESTION.value)
                assertEquals(updatedGame.cells.filter { it.status == CellStatus.CLOSED.value }.size, 5)
            }
        }

        @Test
        fun `when cell is question`() {
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
                markCell(gameId, 0, 2, token)
                val call = markCell(gameId, 0, 2, token)

                val updatedGame: GameData = mapper.readValue(call.response.content!!)

                assertEquals(updatedGame.id, gameId)
                assertEquals(updatedGame.width, 2)
                assertEquals(updatedGame.height, 3)
                assertEquals(updatedGame.status, GameStatus.IN_PROGRESS.value)

                assertEquals(updatedGame.cells.size, 6)
                assertEquals(updatedGame.cells.filter { it.status == CellStatus.CLOSED.value }.size, 6)
            }
        }
    }

    @DisplayName("Open Cell")
    @Nested
    inner class OpenCell {
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

                val call = openCell(gameId, 0, 2, otherUserToken)

                assertEquals(HttpStatusCode.NotFound, call.response.status())
                assertEquals(
                    mapOf("message" to "Resource not found", "errorCode" to 404),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when cell is bomb`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                val createUserCall = createUser("user@example.com", "qwerty")
                val createUserResponse: Map<String, String> = mapper.readValue(createUserCall.response.content!!)

                val token = createUserResponse["token"].toString()

                transaction {
                    Game.new {
                        bombsCount = 1
                        width = 2
                        height = 2
                        user = User.findById(1)!!
                    }

                    (0 until 2).forEach { j ->
                        (0 until 2).forEach { i ->
                            Cell.new {
                                x = i
                                y = j
                                isBomb = (i == 0 && j == 0)
                                bombsNear = if (i == 0 && j == 0) 0 else 1
                                game = Game.findById(1)!!
                            }
                        }
                    }
                }

                // First attempt rescue
                var call = openCell(1, 0, 0, token)
                var updatedGame: GameData = mapper.readValue(call.response.content!!)

                assertEquals(updatedGame.id, 1)
                assertEquals(updatedGame.width, 2)
                assertEquals(updatedGame.height, 2)
                assertEquals(updatedGame.status, GameStatus.IN_PROGRESS.value)

                assertEquals(updatedGame.cells.size, 4)
                assertEquals(updatedGame.cells.find { it.x == 0 && it.y == 0 }?.status, CellStatus.EMPTY.value)

                assertEquals(updatedGame.openingsCount, 1)

                // Second attempt fail
                var bombCell =
                    transaction {
                        Cell.find { Cells.isBomb eq true }.first().data()
                    }

                call = openCell(1, bombCell.x, bombCell.y, token)
                updatedGame = mapper.readValue(call.response.content!!)

                bombCell = updatedGame.cells.find { it.x == bombCell.x && it.y == bombCell.y }!!

                assertEquals(updatedGame.id, 1)
                assertEquals(updatedGame.width, 2)
                assertEquals(updatedGame.height, 2)
                assertEquals(updatedGame.status, GameStatus.FAIL.value)

                assertEquals(updatedGame.cells.size, 4)
                assertEquals(bombCell.status, CellStatus.EXPOSED.value)

                assertEquals(updatedGame.openingsCount, 1)
            }
        }

        @Test
        fun `when cell is not bomb`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                val createUserCall = createUser("user@example.com", "qwerty")
                val createUserResponse: Map<String, String> = mapper.readValue(createUserCall.response.content!!)

                val token = createUserResponse["token"].toString()

                transaction {
                    Game.new {
                        bombsCount = 1
                        width = 4
                        height = 4
                        user = User.findById(1)!!
                    }

                    (0 until 4).forEach { j ->
                        (0 until 4).forEach { i ->
                            Cell.new {
                                x = i
                                y = j
                                isBomb = (i == 0 && j == 0)
                                bombsNear = if (i == 1 && j == 0 || i == 1 && j == 1 || i == 0 && j == 1) 1 else 0
                                game = Game.findById(1)!!
                            }
                        }
                    }
                }

                val call = openCell(1, 3, 3, token)
                val updatedGame: GameData = mapper.readValue(call.response.content!!)

                assertEquals(updatedGame.id, 1)
                assertEquals(updatedGame.width, 4)
                assertEquals(updatedGame.height, 4)
                assertEquals(updatedGame.status, GameStatus.WON.value)

                assertEquals(updatedGame.cells.size, 16)
                assertEquals(updatedGame.cells.find { it.x == 0 && it.y == 0 }?.status, CellStatus.MARKED.value)
                assertEquals(updatedGame.cells.filter { it.status == CellStatus.EMPTY.value }.size, 15)

                assertEquals(updatedGame.openingsCount, 1)
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

fun TestApplicationEngine.openCell(gameId: Int, x: Int, y: Int, token: String): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/games/$gameId/open") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Authorization, "Bearer $token")
        setBody(mapper.writeValueAsString(mapOf("x" to x, "y" to y)))
    }
}
