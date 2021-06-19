package com.mines.users

import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
import com.mines.games.GameData
import com.mines.module
import com.mines.mapper
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert
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
                val call = createUser("user@example.com")

                Assert.assertEquals(HttpStatusCode.OK, call.response.status())
                Assert.assertEquals(
                    mapOf("id" to 1, "email" to "user@example.com", "isAdmin" to false, "games" to emptyList<GameData>(), "password" to "[FILTERED]"),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when user exists`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                createUser("user@example.com")
                val call = createUser("user@example.com")

                Assert.assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                Assert.assertEquals(
                    mapOf("message" to "User exists", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }
}

fun TestApplicationEngine.createUser(email: String): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/users") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())

        setBody(mapper.writeValueAsString(mapOf("email" to email, "password" to "qwerty")))
    }
}
