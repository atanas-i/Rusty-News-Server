package dev.rustybite.data.repository

import dev.rustybite.domain.models.Article
import dev.rustybite.domain.models.Bookmark
import dev.rustybite.domain.models.Favorite
import dev.rustybite.domain.models.Favorites

interface ArticleRepository {
    suspend fun addArticle(article: Article): Article?
    suspend fun getArticle(articleId: String): Article?
    suspend fun getAllArticles(): List<Article>
    suspend fun getBookmarkedArticles(userId: String): List<Article>
    suspend fun bookmarkArticle(articleId: String ,bookmark: Bookmark): Bookmark?
    suspend fun favoriteArticle(articleId: String ,favorite: Favorite): Favorite?
    suspend fun deleteArticle(articleId: String): Boolean
}