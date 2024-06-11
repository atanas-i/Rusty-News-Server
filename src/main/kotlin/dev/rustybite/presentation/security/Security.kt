package dev.rustybite.presentation.security

import io.ktor.utils.io.core.*
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.HexFormat
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.text.toByteArray

private const val ALGORITHM = "PBKDF2WithHmacSHA512"
private const val ITERATION_COUNT = 120_000
private const val KEY_LENGHT = 256
private val secret = System.getenv("HASH_SECRET")
private fun ByteArray.toHexString(): String = HexFormat.of().formatHex(this)

private fun generateRandomSalt(): String {
    val random = SecureRandom()
    val salt = ByteArray(32)
    random.nextBytes(salt)

    return salt.toHexString()
}


fun generateHash(password: String): String {
    val combinedSalt = "${generateRandomSalt()}$secret".toByteArray()
    val factory = SecretKeyFactory.getInstance(ALGORITHM)
    val spec: KeySpec = PBEKeySpec(password.toCharArray(),combinedSalt, ITERATION_COUNT, KEY_LENGHT)
    val key = factory.generateSecret(spec)
    val hash = key.encoded

    return hash.toHexString()
}