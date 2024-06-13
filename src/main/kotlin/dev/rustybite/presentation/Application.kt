package dev.rustybite.presentation

import dev.rustybite.data.database.DatabaseFactory
import dev.rustybite.data.database.configureFlyway
import dev.rustybite.data.repository.ProfileRepository
import dev.rustybite.data.repository.UserRepository
import dev.rustybite.domain.repository.ProfileRepositoryImpl
import dev.rustybite.domain.repository.UserRepositoryImpl
import dev.rustybite.presentation.plugins.configureRouting
import dev.rustybite.presentation.plugins.configureSecurity
import dev.rustybite.presentation.plugins.configureSerialization
import dev.rustybite.presentation.security.JwtService
import dev.rustybite.presentation.security.generateHash
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository: UserRepository = UserRepositoryImpl()
    val profileRepository: ProfileRepository = ProfileRepositoryImpl()
    val service = JwtService()
    val hash = { data: String -> generateHash(data) }

    configureFlyway(this.environment.config)
    DatabaseFactory.initialize(this.environment.config)
    configureSecurity(service)
    configureSerialization()
    configureRouting(
        userRepository,
        profileRepository,
        service,
        hash
    )
}
