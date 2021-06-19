package com.mines

import com.mines.users.UserData
import io.ktor.features.*
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator

class UnprocessableEntityError(message: String) : BadRequestException(message)

@Throws(UnprocessableEntityError::class)
fun <T : Any> T.validate(validator: Validator) {
    validator.
    validate(this).
    takeIf { it.isNotEmpty() }?.
    let { throw UnprocessableEntityError(it.first().messageWithFieldName()) }
}

fun <T : Any> ConstraintViolation<T>.messageWithFieldName() = "${this.propertyPath.toString().capitalize()} ${this.message}".trim()

fun UserData.validate() {
    if (email.isBlank() || password.length < 6) throw UnprocessableEntityError("Credentials must exist. Minimal password length is 6 chars")
}
