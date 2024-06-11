package dev.rustybite.domain.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import java.util.UUID

@Serializable
data class User(
    val userId: String = UUID.randomUUID().toString(),
    val email: String,
    val hashedPassword: String,
)

object Users : Table() {
    val userId = varchar("user_id", 50)
    val email = varchar("email", 50)
    val hashedPassword = varchar("hashed_password", 150)

    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}
