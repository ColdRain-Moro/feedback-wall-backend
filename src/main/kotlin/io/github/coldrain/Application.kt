package io.github.coldrain

import io.github.coldrain.model.database.DatabaseInit
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.github.coldrain.plugins.*

fun main() {
    DatabaseInit.init()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureHTTP()
        configureSerialization()
        configureSockets()
    }.start(wait = true)
}
