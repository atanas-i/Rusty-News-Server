package dev.rustybite.domain.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class User(
    val userId: String = UUID.randomUUID().toString(),
    val email: String,
    val hashedPassword: String,
    val createdAt: String = LocalDateTime.now().toString(),
    val updatedAt: String = LocalDateTime.now().toString(),
)

object Users : Table() {
    val userId = varchar("user_id", 50)
    val email = varchar("email", 50)
    val hashedPassword = varchar("hashed_password", 150)
    val createdAt = varchar("created_at", 150)
    val updatedAt = varchar("updated_at", 150)

    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}
