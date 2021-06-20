package com.mines.settings

import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
import com.mines.mapper
import com.mines.module
import com.mines.users.User
import com.mines.users.authUser
import io.ktor.http.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.*
import org.mindrot.jbcrypt.BCrypt

@DisplayName("Settings routes")
class SettingsTest : ApplicationTest() {
    @DisplayName("Create Settings")
    @Nested
    inner class CreateSettings {
        @Test
        fun `when successful`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = createSettings(10, 10, 2, token)

                assertEquals(HttpStatusCode.OK, call.response.status())
                assertEquals(
                    mapOf("id" to 1, "width" to 10, "height" to 10, "bombsCount" to 2),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when guest try`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                val call = createSettings(10, 10, 2, "")

                assertEquals(HttpStatusCode.Unauthorized, call.response.status())
                assertEquals(
                    mapOf("message" to "Only for registered users", "errorCode" to 401),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when usual user try`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "user@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                    }
                }

                val authCall = authUser("user@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = createSettings(10, 10, 2, token)

                assertEquals(HttpStatusCode.Forbidden, call.response.status())
                assertEquals(
                    mapOf("message" to "You do not have permissions", "errorCode" to 403),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when bombs count is larger or equal than cells count`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = createSettings(10, 10, 100, token)

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Bombs count must be less than cells count", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when width is less than 2`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = createSettings(1, 10, 2, token)

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Width must be greater than or equal to 2", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when height is less than 2`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = createSettings(10, 1, 2, token)

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Height must be greater than or equal to 2", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when settings exist`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                createSettings(10, 10, 2, token)
                val call = createSettings(10, 10, 2, token)

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Settings exist", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }

    @DisplayName("Get Settings")
    @Nested
    inner class GetSettings {
        @Test
        fun `when exist`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }

                    Setting.new {
                        width = 10
                        height = 10
                        bombsCount = 2
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = getSettings(token)

                assertEquals(HttpStatusCode.OK, call.response.status())
                assertEquals(
                    mapOf("id" to 1, "width" to 10, "height" to 10, "bombsCount" to 2),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when doesn't exist`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = getSettings(token)

                assertEquals(HttpStatusCode.NotFound, call.response.status())
                assertEquals(
                    mapOf("message" to "Resource not found", "errorCode" to 404),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }

    @DisplayName("Update Settings")
    @Nested
    inner class UpdateSettings {
        @Test
        fun `when exist`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }

                    Setting.new {
                        width = 10
                        height = 10
                        bombsCount = 2
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                val call = updateSettings(20, 15, 3, token)

                assertEquals(HttpStatusCode.OK, call.response.status())
                assertEquals(
                    mapOf("id" to 1, "width" to 20, "height" to 15, "bombsCount" to 3),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when doesn't exist`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                transaction {
                    User.new {
                        email = "admin@example.com"
                        password = BCrypt.hashpw("qwerty", BCrypt.gensalt())
                        isAdmin = true
                    }
                }

                val authCall = authUser("admin@example.com", "qwerty")
                val authResponse: Map<String, String> = mapper.readValue(authCall.response.content!!)

                val token = authResponse["token"].toString()

                assertEquals(HttpStatusCode.NotFound, getSettings(token).response.status())

                val call = updateSettings(20, 15, 3, token)

                assertEquals(HttpStatusCode.OK, call.response.status())
                assertEquals(
                    mapOf("id" to 1, "width" to 20, "height" to 15, "bombsCount" to 3),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }
}

fun TestApplicationEngine.createSettings(width: Int, height: Int, bombsCount: Int, token: String): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/settings") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Authorization, "Bearer $token")

        setBody(mapper.writeValueAsString(mapOf("width" to width, "height" to height, "bombsCount" to bombsCount)))
    }
}

fun TestApplicationEngine.getSettings(token: String): TestApplicationCall {
    return handleRequest(HttpMethod.Get, "/api/v1/settings") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Authorization, "Bearer $token")
    }
}

fun TestApplicationEngine.updateSettings(width: Int, height: Int, bombsCount: Int, token: String): TestApplicationCall {
    return handleRequest(HttpMethod.Put, "/api/v1/settings") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        addHeader(HttpHeaders.Authorization, "Bearer $token")

        setBody(mapper.writeValueAsString(mapOf("width" to width, "height" to height, "bombsCount" to bombsCount)))
    }
}
