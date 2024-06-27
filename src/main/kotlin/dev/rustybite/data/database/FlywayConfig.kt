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
    val connection = hikariDataSource(config).connection
    val statement = connection.createStatement()
    statement.close()
    connection.close()
    //flyway.repair()
    //flyway.migrate()
}