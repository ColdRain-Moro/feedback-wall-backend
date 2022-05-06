package io.github.coldrain.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.github.coldrain.utils.makeJwtVerifier
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureSecurity() {
    authentication {
        jwt {
            verifier(makeJwtVerifier())
            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }

}
