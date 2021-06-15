package com.mines.api.v1

import com.mines.settings.SettingsService
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.settingsRouter(settingsService: SettingsService) {
    route("/api/v1/settings") {
        post {
            with (call) {
                val params = receiveParameters()

                val width = requireNotNull(params["width"]).toInt()
                val height = requireNotNull(params["height"]).toInt()
                val bombsCount = requireNotNull(params["bombs_count"]).toInt()

                val settings = settingsService.create(width, height, bombsCount)

                respond(settings)
            }
        }
    }
}
