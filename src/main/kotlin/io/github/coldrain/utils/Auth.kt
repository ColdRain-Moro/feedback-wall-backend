package io.github.coldrain.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.time.Instant
import java.util.*

/**
 * io.github.coldrain.utils.Auth
 * feedback-wall-backend
 *
 * @author 寒雨
 * @since 2022/5/6 17:55
 **/
private const val SECRET_KEY = "The life is just the things you love."
private val algorithm = Algorithm.HMAC512(SECRET_KEY)

private const val REFRESH_SECRET_KEY = "The things you love are your life."
private val refreshAlgorithm = Algorithm.HMAC512(REFRESH_SECRET_KEY)
private const val validityInMs = 1000L * 60 * 60 * 2
private const val refreshValidityInMs = 1000L * 60 * 60 * 24 * 30
private const val ISSUER = "feedback-wall-backend"
private const val USER_ID = "userId"

fun makeJwtVerifier(isRefreshToken: Boolean = false): JWTVerifier = JWT
    .require(if (isRefreshToken) refreshAlgorithm else algorithm)
    .withIssuer(ISSUER)
    .build()

fun makeToken(userID: Int, isRefreshToken: Boolean = false): String = JWT.create()
    .withSubject("Authentication")
    .withIssuer(ISSUER)
    .withIssuedAt(Date())
    .withClaim(USER_ID, userID)
    .withExpiresAt(getExpiration(isRefreshToken))
    .sign(if (isRefreshToken) refreshAlgorithm else algorithm)

private fun getExpiration(isRefreshToken: Boolean) =
    Date(Instant.now().toEpochMilli() + if (isRefreshToken) refreshValidityInMs else validityInMs)


fun ApplicationCall.getUserID(): Long? {
    val principal = principal<JWTPrincipal>()
    return principal?.payload?.getClaim(USER_ID)?.asLong()
}

