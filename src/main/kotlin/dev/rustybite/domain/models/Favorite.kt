package dev.rustybite.domain.models

import org.jetbrains.exposed.sql.Table
import java.util.UUID

data class Favorite(
    val favoriteId: String = UUID.randomUUID().toString(),
    val articleId: String,
    val userId: String,
)

object Favorites : Table() {
    val favoriteId = varchar("favorite_id", 150)
    val articleId = varchar("article_id", 150)
    val userId = varchar("user_id", 150)
}