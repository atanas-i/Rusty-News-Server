package dev.rustybite.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*

fun hikariDataSource(appConfig: ApplicationConfig): HikariDataSource {
    val hikariConfig = HikariConfig()
    hikariConfig.jdbcUrl = System.getenv("JDBC_URL")//appConfig.property("storage.jdbcUrl").getString()
    hikariConfig.driverClassName = System.getenv("JDBC_DRIVER")

    return HikariDataSource(hikariConfig)
}