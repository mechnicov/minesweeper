package com.mines.api.v1

import com.mines.jwt.checkIsAdmin
import com.mines.settings.SettingsData
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
            call.checkIsAdmin()

            val settings = settingsService.get()
            call.respond(settings)
        }

        post {
            call.checkIsAdmin()

            var settings = call.receive<SettingsData>()
            settings.validate(validator)
            settings = settingsService.create(settings.width, settings.height, settings.bombsCount)
            call.respond(settings)
        }

        put {
            call.checkIsAdmin()

            var settings = call.receive<SettingsData>()
            settings.validate(validator)
            settings = settingsService.update(settings.width, settings.height, settings.bombsCount)
            call.respond(settings)
        }
    }
}
