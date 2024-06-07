package dev.rustybite.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.flywaydb.core.Flyway

fun configureFlyway(config: ApplicationConfig) {
    val flyway = Flyway.configure()
        .dataSource(hikariDataSource(config))
        .load()
    flyway.migrate()
}