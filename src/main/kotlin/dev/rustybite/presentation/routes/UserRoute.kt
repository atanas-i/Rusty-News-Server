package dev.rustybite.presentation.routes

import dev.rustybite.data.repository.ProfileRepository
import dev.rustybite.data.repository.UserRepository
import dev.rustybite.domain.models.Response
import dev.rustybite.domain.models.User
import dev.rustybite.domain.models.UserCredentials
import dev.rustybite.domain.models.UserProfile
import dev.rustybite.presentation.security.JwtService
import dev.rustybite.presentation.security.validatePassword
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
            val regRequest = try {
                call.receive<User>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Response(
                        success = false,
                        message = e.localizedMessage
                    )
                )
                return@post
            }
            val hashedUser = User(
                userId = regRequest.userId,
                email = regRequest.email,
                hashedPassword = hashFunction(regRequest.hashedPassword)
            )
            val user = userRepository.createUser(hashedUser)
            try {
                if (user != null) {
                    val profile = UserProfile(
                        userId = user.userId,
                        email = user.email
                    )
                    profileRepository.createProfile(profile)
                    call.respond(
                        HttpStatusCode.Created,
                        Response(
                           success = true,
                            message = service.generateToken(user)
                        )
                    )
                } else {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        Response(success = false, message = "Failed to create user!"),
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    Response(success = false, message = e.localizedMessage),
                )
            }
        }
        post("login") {
            val credentials = try {
                call.receive<UserCredentials>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Response(success = false, message = e.localizedMessage)
                )
                return@post
            }
            try {
                val user = userRepository.loginUser(credentials.email)
                if (user != null) {
                    if (validatePassword(credentials.password, user.hashedPassword)) {
                        call.respond(
                            HttpStatusCode.OK,
                            Response(success = true, message = service.generateToken(user))
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            Response(success = false, message = "Invalid password!"),
                        )
                    }
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Response(success = true, message = "User not found!"),
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Response(success = false, message = e.localizedMessage),
                )
            }
        }
        patch("/emails/{id}") {
            val response = call.response
            val email = try {
                call.receive<String>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Response(success = true, message = e.localizedMessage)
                )
                return@patch
            }
            val id = call.parameters["id"] ?: return@patch call.respond(
                HttpStatusCode.NotFound,
                Response(success = false, message = "No user with such Id!")
            )
            if (userRepository.updateEmail(id, email)) {
                call.respond(
                    HttpStatusCode.OK,
                    Response(success = true, message = "Email updated successfully!"),
                )
            } else {
                call.respond(
                    response.status() ?: HttpStatusCode.BadRequest,
                    Response(success = false, message = "An error occurred. Failed to update an email")
                )
            }
        }
        patch("/security/{id}") {
            val receivedPassword = try {
                call.receive<String>()
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.NotFound,
                    Response(success = false, message = e.localizedMessage)
                )
                return@patch
            }
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
            try {
                val id = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.NotFound,
                    Response(success = false, message = "No user with such Id!")
                )
                profileRepository.deleteProfile(id)
                userRepository.deleteUser(id)
                call.respond(
                    HttpStatusCode.OK,
                    Response(success = true, message = "User deleted successfully!")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    Response(success = false, message = e.localizedMessage)
                )
            }
        }
    }
}