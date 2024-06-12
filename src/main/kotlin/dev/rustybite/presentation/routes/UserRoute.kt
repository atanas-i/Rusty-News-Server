package dev.rustybite.presentation.routes

import dev.rustybite.data.repository.ProfileRepository
import dev.rustybite.data.repository.UserRepository
import dev.rustybite.domain.models.Response
import dev.rustybite.domain.models.User
import dev.rustybite.domain.models.UserProfile
import dev.rustybite.presentation.security.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(
    userRepository: UserRepository,
    profileRepository: ProfileRepository,
    service: JwtService,
    hashFunction: (String) -> String
) {
    route("user") {
        post("/registration") {
            val data = call.receive<User>()
            val hashedUser = User(
                userId = data.userId,
                email = data.email,
                hashedPassword = hashFunction(data.hashedPassword)
            )
            service.generateToken(hashedUser)
            val user = userRepository.createUser(hashedUser)
            if (user != null) {
                val profile = UserProfile(
                    userId = user.userId,
                    email = user.email
                )
                profileRepository.createProfile(profile)
                call.respond(
                    status = HttpStatusCode.Created,
                    Response(success = true, message = "Successfully created user!"),
                )
            } else {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    Response(success = false, message = "Failed to create user!"),
                )
            }
        }
        patch("/emails/{id}") {
            val email = call.receive<String>()
            val id = call.parameters["id"] ?: return@patch call.respond(
                HttpStatusCode.NotFound,
                Response(success = false, message = "No user with such Id!")
            )
            userRepository.updatePassword(id, email)
            call.respond(
                HttpStatusCode.OK,
                Response(success = true, message = "Email updated successfully!")
            )
        }
        patch("/security/{id}") {
            val receivedPassword = call.receive<String>()
            val hashedPassword = hashFunction(receivedPassword)
            val id = call.parameters["id"] ?: return@patch call.respond(
                HttpStatusCode.NotFound,
                Response(success = false, message = "No user with such Id!")
            )
            userRepository.updatePassword(id, hashedPassword)
            call.respond(
                HttpStatusCode.OK,
                Response(success = true, message = "Password updated successfully!")
            )
        }
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.NotFound,
                Response(success = false, message = "No user with such Id!")
            )
            userRepository.deleteUser(id)
            call.respond(
                HttpStatusCode.OK,
                Response(success = true, message = "User deleted successfully!")
            )
        }
    }
}