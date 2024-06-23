package dev.rustybite.domain.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class Article(
    val articleId: String = UUID.randomUUID().toString(),
    val articleTitle: String,
    val articleBody: String,
    val author: String,
    val publishedAt: String = LocalDateTime.now().toString(),
    val isBookmarked: Boolean = false,
    val isFavorite: Boolean = false,
    val categories: List<String>,
)

object Articles : Table() {
    val articleId = varchar("article_id", 150)
    val articleTitle = varchar("article_title", 150)
    val articleBody = text("article_body")
    val author = varchar("author", 50)
    val publishedAt = varchar("published_at", 150)
    val isBookmarked = bool("is_bookmarked")
    val isFavorite = bool("is_favorite")
    val categories = array<String>("categories")
}