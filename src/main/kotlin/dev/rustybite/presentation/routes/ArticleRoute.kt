package dev.rustybite.presentation.routes

import dev.rustybite.data.repository.ArticleRepository
import dev.rustybite.data.repository.UserRepository
import dev.rustybite.domain.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.IOException

fun Route.articleRoute(
    articleRepository: ArticleRepository,
    userRepository: UserRepository
) {
    route("/article") {
        authenticate("auth-jwt") {
            post {
                val article = try {
                    call.receive<Article>()
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        Response(success = true, message = e.localizedMessage)
                    )
                    return@post
                }
                val result = articleRepository.addArticle(article)
                try {
                    if (result != null) {
                        call.respond(
                            HttpStatusCode.OK,
                            Response(success = true, message = "Article added successfully")
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            Response(success = false, message = "Failed to add article")
                        )
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                } catch (e: IOException) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                }
            }
        }
        get {
            try {
                val articles = articleRepository.getAllArticles()
                if (articles.isEmpty()) {
                    call.respond(HttpStatusCode.OK, Response(success = true, message = "There are no articles"))
                } else {
                    call.respond(HttpStatusCode.OK, articles)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
            } catch (e: IOException) {
                call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
            }
        }
        authenticate("auth-jwt") {
            get("bookmark") {
                try {
                    var bookmarks = emptyList<Article>()
                    val userEmail = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    if (userEmail != null) {
                        val user = userRepository.getUserByEmail(userEmail)
                        if (user != null) {
                            bookmarks = articleRepository.getBookmarkedArticles(user.userId)
                            call.respond(HttpStatusCode.OK, bookmarks)
                        } else {
                            call.respond(
                                HttpStatusCode.Unauthorized,
                                Response(success = false, "Please login to get bookmarks")
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            Response(success = false, "There is no email")
                        )
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                } catch (e: IOException) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                }
            }
        }
        get("/{id}") {
            try {
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    Response(success = false, message = "There is no such id")
                )
                val article = articleRepository.getArticle(id)
                if (article != null) {
                    call.respond(HttpStatusCode.OK, article)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        Response(success = false, message = "There is no such article")
                    )
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
            } catch (e: IOException) {
                call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
            }
        }
        authenticate("auth-jwt") {
            post("/{id}/bookmark") {
                try {
                    val id = call.parameters["id"] ?: return@post call.respond(
                        HttpStatusCode.NotFound,
                        Response(success = false, message = "There is no such id")
                    )
                   val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    if (email != null) {
                        val user = userRepository.getUserByEmail(email)
                        if (user != null) {
                            val article = articleRepository.getArticle(id)
                            if (article != null) {
                                val bookmark = Bookmark(articleId = article.articleId, userId = user.userId)
                                val result = articleRepository.bookmarkArticle(article.articleId, bookmark)
                                if (result != null) {
                                    call.respond(
                                        HttpStatusCode.OK,
                                        Response(success = true, message = "Article bookmarked successfully")
                                    )
                                } else {
                                    call.respond(
                                        HttpStatusCode.BadRequest,
                                        Response(success = false, "Failed to bookmark an article")
                                    )
                                }
                            } else {
                                call.respond(
                                    HttpStatusCode.NotFound,
                                    Response(success = false, message = "There is no article with this id")
                                )
                            }
                        } else {
                            call.respond(
                                HttpStatusCode.NotFound,
                                Response(success = false, message = "There is no user with this email")
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            Response(success = false, message = "Please login to bookmark this article")
                        )
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                } catch (e: IOException) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                }
            }
        }
        authenticate("auth-jwt") {
            post("/{id}/favorite") {
                try {
                    val id = call.parameters["id"] ?: return@post call.respond(
                        HttpStatusCode.NotFound,
                        Response(success = false, message = "There is no such id")
                    )
                    val email = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()
                    if (email != null) {
                        val user = userRepository.getUserByEmail(email)
                        if (user != null) {
                            val article = articleRepository.getArticle(id)
                            if (article != null) {
                                val favorite = Favorite(articleId = article.articleId, userId = user.userId)
                                val result = articleRepository.favoriteArticle(article.articleId, favorite)
                                if (result != null) {
                                    call.respond(
                                        HttpStatusCode.OK,
                                        Response(success = true, message = "Article added to favorites")
                                    )
                                } else {
                                    call.respond(
                                        HttpStatusCode.BadRequest,
                                        Response(success = false, "Failed to add to favorites")
                                    )
                                }
                            } else {
                                call.respond(
                                    HttpStatusCode.NotFound,
                                    Response(success = false, message = "There is no article with this id")
                                )
                            }
                        } else {
                            call.respond(
                                HttpStatusCode.NotFound,
                                Response(success = false, message = "There is no user with this email")
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            Response(success = false, message = "Please login to add this article to favorites")
                        )
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                } catch (e: IOException) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                }
            }
        }
        authenticate("auth-jwt") {
            delete("/{id}") {
                try {
                    val id = call.parameters["id"] ?: return@delete call.respond(
                        HttpStatusCode.NotFound,
                        Response(success = false, message = "There is no such id")
                    )
                    if (articleRepository.deleteArticle(id)) {
                        call.respond(
                            HttpStatusCode.OK,
                            Response(success = true, message = "Article deleted successfully")
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            Response(success = false, message = "There is no article with this id")
                        )
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                } catch (e: IOException) {
                    call.respond(HttpStatusCode.BadRequest, Response(success = false, message = e.localizedMessage))
                }
            }
        }
    }
}