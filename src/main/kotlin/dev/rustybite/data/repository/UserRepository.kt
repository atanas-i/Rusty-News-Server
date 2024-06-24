package dev.rustybite.data.repository

import dev.rustybite.domain.models.User

interface UserRepository {
    suspend fun createUser(user: User): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun updatePassword(userId: String, newHashPassword: String): Boolean
    suspend fun updateEmail(userId: String, newEmail: String): Boolean
    suspend fun deleteUser(userId: String): Boolean
}