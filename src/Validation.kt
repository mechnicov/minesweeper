package com.mines

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

data class ValidationError(val message: String, val errorCode: Int)
