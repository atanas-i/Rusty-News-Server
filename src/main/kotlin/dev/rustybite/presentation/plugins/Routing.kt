package dev.rustybite.presentation.plugins

import dev.rustybite.data.repository.ProfileRepository
import dev.rustybite.data.repository.UserRepository
import dev.rustybite.domain.models.UserProfile
import dev.rustybite.presentation.routes.userRoute
import dev.rustybite.presentation.security.JwtService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userRepository: UserRepository,
    profileRepository: ProfileRepository,
    service: JwtService,
    hashFunction: (String) -> String
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoute(
            userRepository,
            profileRepository,
            service,
            hashFunction
        )
    }
}
