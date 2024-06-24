package dev.rustybite.domain.repository

import dev.rustybite.data.database.DatabaseFactory.dbQuery
import dev.rustybite.data.repository.UserRepository
import dev.rustybite.domain.models.User
import dev.rustybite.domain.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.StatementType
import java.time.LocalDateTime

class UserRepositoryImpl : UserRepository {
    private fun rowToUser(row: ResultRow): User = User(
        userId = row[Users.userId],
        email = row[Users.email],
        hashedPassword = row[Users.hashedPassword],
        createdAt = row[Users.createdAt],
        updatedAt = row[Users.updatedAt]
    )

    override suspend fun createUser(user: User): User? = dbQuery {
        val insertStatement = Users.insert { statement ->
            statement[userId] = user.userId
            statement[email] = user.email
            statement[hashedPassword] = user.hashedPassword
            statement[createdAt] = user.createdAt
            statement[updatedAt] = user.updatedAt
       }
        insertStatement.resultedValues?.firstOrNull()?.let { rowToUser(it) }
    }

    override suspend fun getUserByEmail(email: String): User? = dbQuery {
        Users.select(where = { Users.email eq email }).singleOrNull()?.let { rowToUser(it) }
    }
    override suspend fun updatePassword(userId: String, newHashPassword: String): Boolean = dbQuery {
        Users.update(where = {Users.userId eq userId}) { statement ->
            statement[hashedPassword] = newHashPassword
            statement[updatedAt] = LocalDateTime.now().toString()
        } > 0
    }

    override suspend fun updateEmail(userId: String, newEmail: String): Boolean = dbQuery {
        Users.update(where = {Users.userId eq userId}) { statement ->
            statement[email] = newEmail
            statement[updatedAt] = LocalDateTime.now().toString()
        } > 0
    }

    override suspend fun deleteUser(userId: String): Boolean = dbQuery {
        Users.deleteWhere { Users.userId eq userId } > 0
    }
}