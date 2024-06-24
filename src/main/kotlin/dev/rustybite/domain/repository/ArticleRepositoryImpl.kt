package dev.rustybite.domain.repository

import dev.rustybite.data.database.DatabaseFactory.dbQuery
import dev.rustybite.data.repository.ArticleRepository
import dev.rustybite.domain.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.DeleteStatement.Companion.where

class ArticleRepositoryImpl : ArticleRepository {
    private fun rowToArticle(row: ResultRow): Article = Article(
        articleId = row[Articles.articleId], articleTitle = row[Articles.articleTitle],
        articleBody = row[Articles.articleBody], author = row[Articles.author],
        publishedAt = row[Articles.publishedAt], isBookmarked = row[Articles.isBookmarked],
        isFavorite = row[Articles.isFavorite], category = row[Articles.category]
    )
    private fun rowToBookmark(row: ResultRow): Bookmark = Bookmark(
        bookmarkId = row[Bookmarks.bookmarkId],
        articleId = row[Bookmarks.articleId],
        userId = row[Bookmarks.userId],
        bookmarkedAt = row[Bookmarks.bookmarkedAt],
    )
    private fun rowToFavorite(row: ResultRow): Favorite = Favorite(
        favoriteId = row[Favorites.favoriteId],
        articleId = row[Favorites.articleId],
        userId = row[Favorites.userId],
    )
    override suspend fun addArticle(article: Article): Article? = dbQuery {
        val insertStatement = Articles.insert{ statement ->
            statement[Articles.articleId] = article.articleId
            statement[Articles.articleTitle] = article.articleTitle
            statement[articleBody] = article.articleBody
            statement[author] = article.author
            statement[publishedAt] = article.publishedAt
            statement[isBookmarked] = article.isBookmarked
            statement[isFavorite] = article.isFavorite
            statement[category] = article.category
        }

        insertStatement.resultedValues?.singleOrNull()?.let { rowToArticle(it) }
    }

    override suspend fun getArticle(articleId: String): Article? = dbQuery {
        Articles.selectAll().where { Articles.articleId eq articleId }.firstOrNull()?.let { rowToArticle(it) }
    }

    override suspend fun getAllArticles(): List<Article> = dbQuery {
        Articles.selectAll().map { rowToArticle(it) }
    }

    override suspend fun getBookmarkedArticles(userId: String): List<Article> = dbQuery {
        val bookmarks = Bookmarks.selectAll().where { Bookmarks.userId eq userId }.map { rowToBookmark(it) }
        val articleIds = bookmarks.map { it.articleId }
        Articles.selectAll().where { Articles.articleId inList articleIds }.map { rowToArticle(it) }
    }

    override suspend fun bookmarkArticle(articleId: String ,bookmark: Bookmark): Bookmark? = dbQuery {
        val insertStatement = Bookmarks.insert { statement ->
            statement[bookmarkId] = bookmark.bookmarkId
            statement[Bookmarks.articleId] = bookmark.articleId
            statement[userId] = bookmark.userId
            statement[bookmarkedAt] = bookmark.bookmarkedAt
        }
        Articles.update(where = {Articles.articleId eq articleId}) {statement ->
            statement[isBookmarked] = true
        }
        insertStatement.resultedValues?.singleOrNull()?.let { rowToBookmark(it) }
    }

    override suspend fun favoriteArticle(articleId: String ,favorite: Favorite): Favorite? = dbQuery {
        val insertStatement = Favorites.insert { statement ->
            statement[favoriteId] = favorite.favoriteId
            statement[Favorites.articleId] = favorite.articleId
            statement[userId] = favorite.userId
        }
        Articles.update(where = {Articles.articleId eq articleId}) { statement ->
            statement[isFavorite] = true
        }
        insertStatement.resultedValues?.singleOrNull()?.let { rowToFavorite(it) }
    }

    override suspend fun deleteArticle(articleId: String): Boolean = dbQuery {
        Articles.deleteWhere { Articles.articleId eq articleId } > 0
    }
}