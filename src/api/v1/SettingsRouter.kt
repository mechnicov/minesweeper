package com.mines.api.v1

import com.mines.settings.Setting
import com.mines.settings.SettingsService
import com.mines.validate
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import jakarta.validation.Validation

fun Route.settingsRouter(settingsService: SettingsService) {
    route("/api/v1/settings") {
        val validator = Validation.buildDefaultValidatorFactory().validator

        get {
            val settings = settingsService.get()
            call.respond(settings)
        }

        post {
            var settings = call.receive<Setting>()
            settings.validate(validator)
            settings = settingsService.create(settings.width, settings.height, settings.bombsCount)
            call.respond(settings)
        }
    }
}
