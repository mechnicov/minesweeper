package com.mines.jwt

import io.github.cdimascio.dotenv.dotenv
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JWTConfig {
    private val secret = dotenv["JWT_SECRET"]
    private val issuer = dotenv["JWT_ISSUER"]
    private val validityInMs = dotenv["JWT_EXPIRED"].toInt()
    private val algorithm = Algorithm.HMAC512(secret)

    fun generateToken(user: Login): String =
        JWT.
        create().
        withSubject("Authentication").
        withIssuer(issuer).
        withClaim("email", user.email).
        withClaim("password", user.password).
        withExpiresAt(getExpiration()).
        sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}

var dotenv = dotenv()
