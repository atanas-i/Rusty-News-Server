package dev.rustybite.presentation.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.rustybite.domain.models.User
import dev.rustybite.domain.models.Users
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.Database
import java.util.*

class JwtService {
    private val secret = System.getenv("JWT_SECRET")
    private val issuer = System.getenv("ISSUER")
    private val audience = System.getenv("AUDIENCE")
    private val realm = System.getenv("REALM")
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()

    fun generateToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(algorithm)
    }

    fun validate(credential: JWTCredential): JWTPrincipal? {
        return if (credential.payload.getClaim("email").asString() != "") {
            JWTPrincipal(credential.payload)
        } else {
            null
        }
    }
}