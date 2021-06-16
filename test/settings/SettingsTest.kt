package com.mines.settings

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mines.ApplicationTest
import com.mines.module
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.*

@DisplayName("Settings routes")
class SettingsTest : ApplicationTest() {
    @DisplayName("Create Settings")
    @Nested
    inner class CreateSettings {
        @Test
        fun `when successful`() {
            withTestApplication(Application::module) {
                val call = createSettings(10, 10, 2)

                assertEquals(HttpStatusCode.OK, call.response.status())
                assertEquals(
                    mapOf("id" to 1, "width" to 10, "height" to 10, "bombsCount" to 2),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when bombs count is larger or equal than cells count`() {
            withTestApplication(Application::module) {
                val call = createSettings(10, 10, 100)

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Bombs count must be less than cells count", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when width is less than 2`() {
            withTestApplication(Application::module) {
                val call = createSettings(1, 10, 2)

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Width must be greater than or equal to 2", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when height is less than 2`() {
            withTestApplication(Application::module) {
                val call = createSettings(10, 1, 2)

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Height must be greater than or equal to 2", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }

        @Test
        fun `when settings exist`() {
            withTestApplication(Application::module) {
                createSettings(10, 10, 2)
                val call = createSettings(10, 10, 2)

                assertEquals(HttpStatusCode.UnprocessableEntity, call.response.status())
                assertEquals(
                    mapOf("message" to "Settings exist", "errorCode" to 422),
                    mapper.readValue(call.response.content!!)
                )
            }
        }
    }
}

val mapper = jacksonObjectMapper()

fun TestApplicationEngine.createSettings(width: Int, height: Int, bombsCount: Int): TestApplicationCall {
    return handleRequest(HttpMethod.Post, "/api/v1/settings") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())

        setBody(mapper.writeValueAsString(mapOf("width" to width, "height" to height, "bombsCount" to bombsCount)))
    }
}
