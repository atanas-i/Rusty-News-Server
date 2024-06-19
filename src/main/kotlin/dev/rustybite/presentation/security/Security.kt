package dev.rustybite.presentation.security

import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


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

fun validatePassword(password: String, storedHashedPassword: String): Boolean {
    val salt = Base64.getDecoder().decode(getSalt(storedHashedPassword))
    val hash = Base64.getDecoder().decode(getHashedPassword(storedHashedPassword))
    val keySpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGHT)
    val factory = SecretKeyFactory.getInstance(ALGORITHM)
    val computedHash = factory.generateSecret(keySpec).encoded

    return slowEquals(computedHash, hash)
}

private fun getSalt(storedHashedPassword: String): String {
    val parts = storedHashedPassword.split(":")
    return parts[0]
}

private fun getHashedPassword(storedHashedPassword: String): String {
    val parts = storedHashedPassword.split(":")
    return parts[1]
}

private fun slowEquals(a: ByteArray, b: ByteArray): Boolean {
    var diff = a.size xor b.size
    var i = 0
    while (i < a.size && i < b.size) {
        diff = diff or (a[i].toInt() xor b[i].toInt())
        i++
    }
    return diff == 0
}





