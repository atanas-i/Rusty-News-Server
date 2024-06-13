package dev.rustybite.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val success: Boolean,
    val message: String,
)
