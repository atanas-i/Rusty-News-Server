package dev.rustybite.domain.repository

import dev.rustybite.data.database.DatabaseFactory.dbQuery
import dev.rustybite.data.repository.ProfileRepository
import dev.rustybite.domain.models.Profiles
import dev.rustybite.domain.models.UserProfile
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ProfileRepositoryImpl : ProfileRepository {
    private fun rowToProfile(row: ResultRow): UserProfile = UserProfile(
        profileId = row[Profiles.profileId], userId = row[Profiles.userId],
        firstName = row[Profiles.firstName], lastName = row[Profiles.lastName],
        email = row[Profiles.email], userName = row[Profiles.userName],
        userProfilePicture = row[Profiles.userProfilePicture]
    )
    override suspend fun getProfile(userId: String): UserProfile? = dbQuery {
        Profiles.select(where = Profiles.userId eq userId).singleOrNull()?.let { rowToProfile(it) }
    }

    override suspend fun createProfile(profile: UserProfile): UserProfile? = dbQuery {
        val insertStatement = Profiles.insert { insertStatement ->
            insertStatement[profileId] = profile.profileId
            insertStatement[userId] = profile.userId
            insertStatement[firstName] = profile.firstName
            insertStatement[lastName] = profile.lastName
            insertStatement[email] = profile.email
            insertStatement[userName] = profile.userName
            insertStatement[userProfilePicture] = profile.userProfilePicture
        }
        insertStatement.resultedValues?.singleOrNull()?.let { rowToProfile(it) }
    }

    override suspend fun updateProfile(profile: UserProfile): Boolean = dbQuery {
        Profiles.update(where = {Profiles.userId eq profile.userId }) { updateStatement ->
            updateStatement[profileId] = profile.profileId
            updateStatement[userId] = profile.userId
            updateStatement[firstName] = profile.firstName
            updateStatement[lastName] = profile.lastName
            updateStatement[email] = profile.email
            updateStatement[userName] = profile.userName
            updateStatement[userProfilePicture] = profile.userProfilePicture
        } > 0
    }

    override suspend fun deleteProfile(userId: String): Boolean = dbQuery {
        Profiles.deleteWhere { Profiles.userId eq userId } > 0
    }
}