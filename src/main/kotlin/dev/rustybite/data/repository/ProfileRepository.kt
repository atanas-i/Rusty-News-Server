package dev.rustybite.data.repository

import dev.rustybite.domain.models.User
import dev.rustybite.domain.models.UserProfile

interface ProfileRepository {
    suspend fun getProfile(userId: String): UserProfile?
    suspend fun createProfile(profile: UserProfile): UserProfile?
    suspend fun updateProfile(profile: UserProfile): Boolean
    suspend fun deleteProfile(userId: String): Boolean
}