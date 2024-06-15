package dev.rustybite.data.repository

import dev.rustybite.domain.models.User

interface UserRepository {
    suspend fun getUser(userId: String): User?
    suspend fun createUser(user: User): User?
    suspend fun updatePassword(userId: String, newHashPassword: String): Boolean
    suspend fun updateEmail(userId: String, newEmail: String): Boolean
    suspend fun deleteUser(userId: String): Boolean
}