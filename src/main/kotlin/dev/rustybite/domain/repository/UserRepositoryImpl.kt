package dev.rustybite.domain.repository

import dev.rustybite.data.database.DatabaseFactory.dbQuery
import dev.rustybite.data.repository.UserRepository
import dev.rustybite.domain.models.User
import dev.rustybite.domain.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.StatementType

class UserRepositoryImpl : UserRepository {
    private fun rowToUser(row: ResultRow): User = User(
        userId = row[Users.userId],
        email = row[Users.email],
        hashedPassword = row[Users.hashedPassword]
    )

    override suspend fun getUser(userId: String): User? = dbQuery {
        Users.select(where = Users.userId eq userId).singleOrNull()?.let { rowToUser(it) }
    }
    override suspend fun createUser(user: User): User? = dbQuery {
        val insertStatement = Users.insert { statement ->
            statement[userId] = user.userId
            statement[email] = user.email
            statement[hashedPassword] = user.userId
       }
        insertStatement.resultedValues?.firstOrNull()?.let { rowToUser(it) }
    }

    override suspend fun updatePassword(userId: String, newHashPassword: String): Boolean = dbQuery {
        Users.update(where = {Users.userId eq userId}) { statement ->
            statement[hashedPassword] = newHashPassword
        } > 0
    }

    override suspend fun updateEmail(userId: String, newEmail: String): Boolean = dbQuery {
        Users.update(where = {Users.userId eq userId}) { statement ->
            statement[email] = newEmail
        } > 0
    }

    override suspend fun deleteUser(userId: String): Boolean = dbQuery {
        Users.deleteWhere { Users.userId eq userId } > 0
    }
}