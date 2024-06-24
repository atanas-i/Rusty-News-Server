package dev.rustybite.domain.models

import org.jetbrains.exposed.sql.Table
import java.time.LocalDateTime
import java.util.UUID

data class Bookmark(
    val bookmarkId: String = UUID.randomUUID().toString(),
    val articleId: String,
    val userId: String,
    val bookmarkedAt: String = LocalDateTime.now().toString(),
)

object Bookmarks : Table() {
    val bookmarkId = varchar("bookmark_id", 150)
    val articleId = varchar("article_id", 150)
    val userId = varchar("user_id", 150)
    val bookmarkedAt = varchar("bookmarked_at", 150)
}