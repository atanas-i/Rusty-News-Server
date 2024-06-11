package dev.rustybite.data.database

import dev.rustybite.domain.models.Profiles
import dev.rustybite.domain.models.Users
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun initialize(appConfig: ApplicationConfig) {
        Database.connect(hikariDataSource(appConfig))
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Profiles)
        }
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}