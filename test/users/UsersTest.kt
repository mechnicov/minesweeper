package com.mines.users

import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
import com.mines.games.GameData
import com.mines.module
import com.mines.mapper
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Users routes")
class UsersTest : ApplicationTest() {
    @DisplayName("Create User")
    @Nested
    inner class CreateUser {
        @Test
        fun `when successful`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                val call = createUser("user@example.com", "qwerty")

                val response: Map<String, String> = mapper.readValue(call.response.content!!)

                assertEquals(HttpStatusCode.OK, call.response.status())
                assertTrue(response.containsKey("token"))
            }
        }

        @Test
        fun `when user exists`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                createUser("user@example.com", "qwerty")
                val call = createUser("user@example.com", "qwerty")

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "User exists", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }

    @DisplayName("Authenticate User")
    @Nested
    inner class AuthenticateUser {
        @Test
        fun `when successful`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                createUser("user@example.com", "qwerty")
                val call = authUser("user@example.com", "qwerty")

                val response: Map<String, String> = mapper.readValue(call.response.content!!)

                assertEquals(HttpStatusCode.OK, call.response.status())
                assertTrue(response.containsKey("token"))
            }
        }

        @Test
        fun `when there is no such user`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                createUser("user@example.com", "qwerty")
                val call = authUser("other@example.com", "qwerty")

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Bad credentials", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when password is wrong`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                createUser("user@example.com", "qwerty")
                val call = authUser("user@example.com", "111111")

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Bad credentials", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }

    @DisplayName("Get current user")
    @Nested
    inner class GetCurrentUser {
        @Test
        fun `successfully`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                val createCall = createUser("user@example.com", "qwerty")
                val createResponse: Map<String, String> = mapper.readValue(createCall.response.content!!)

                val token = createResponse["token"]

                val call = getCurrentUser(token.toString())
                assertEquals(HttpStatusCode.OK, call.response.status())
                assertEquals(
                    mapOf("id" to 1, "email" to "user@example.com", "isAdmin" to false, "games" to emptyList<GameData>(), "password" to "[FILTERED]"),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }
}

fun TestApplicationEngine.createUser(email: String, password: String): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/users") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())

        setBody(mapper.writeValueAsString(mapOf("email" to email, "password" to password)))
    }
}

fun TestApplicationEngine.authUser(email: String, password: String): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/auth") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())

        setBody(mapper.writeValueAsString(mapOf("email" to email, "password" to password)))
    }
}

fun TestApplicationEngine.getCurrentUser(token: String): TestApplicationCall {
    return handleRequest(HttpMethod.Get, "/api/v1/auth") {
        addHeader(HttpHeaders.Authorization, "Bearer $token")
    }
}
