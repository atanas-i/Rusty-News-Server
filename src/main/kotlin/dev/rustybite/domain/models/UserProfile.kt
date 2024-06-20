package dev.rustybite.domain.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class UserProfile(
    val profileId: String = UUID.randomUUID().toString(),
    val userId: String,
    val firstName: String = "",
    val lastName: String = "",
    val email: String,
    val userName: String = "",
    val createdAt: String = LocalDateTime.now().toString(),
    val updatedAt: String = LocalDateTime.now().toString(),
    val userProfilePicture: String = "",
)

object Profiles : Table() {
    val profileId = varchar("profile_id", 50)
    val userId = reference("user_id", Users.userId)
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 50)
    val userName = varchar("user_name", 50)
    val createdAt = varchar("created_at", 150)
    val updatedAt = varchar("updated_at", 150)
    val userProfilePicture = varchar("user_profile_picture", 50)

    override val primaryKey = PrimaryKey(profileId)
}

