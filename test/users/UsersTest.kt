package com.mines.users

import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
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
                val call = createUser("user@example.com", "qwerty")

                val response: Map<String, String> = mapper.readValue(call.response.content!!)

                Assert.assertEquals(HttpStatusCode.OK, call.response.status())
                Assert.assertTrue(response.containsKey("token"))
            }
        }

        @Test
        fun `when user exists`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                createUser("user@example.com", "qwerty")
                val call = createUser("user@example.com", "qwerty")

                Assert.assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                Assert.assertEquals(
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

                Assert.assertEquals(HttpStatusCode.OK, call.response.status())
                Assert.assertTrue(response.containsKey("token"))
            }
        }

        @Test
        fun `when there is no such user`() {
            withTestApplication(moduleFunction = { module(testing = true) }) {
                createUser("user@example.com", "qwerty")
                val call = authUser("other@example.com", "qwerty")

                Assert.assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                Assert.assertEquals(
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

                Assert.assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                Assert.assertEquals(
                    mapOf("message" to "Bad credentials", "errorCode" to 422),
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
