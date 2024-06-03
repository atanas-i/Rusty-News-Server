package dev.rustybite.presentation

import dev.rustybite.presentation.plugins.configureRouting
import dev.rustybite.presentation.plugins.configureSecurity
import dev.rustybite.presentation.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()
}
