package dev.rustybite.presentation.security

import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


private const val ALGORITHM = "PBKDF2WithHmacSHA256"
private const val ITERATION_COUNT = 120_000
private const val KEY_LENGHT = 256
private val secret = System.getenv("HASH_SECRET")
private fun ByteArray.toHexString(): String = HexFormat.of().formatHex(this)

private fun generateRandomSalt(): ByteArray {
    val random = SecureRandom()
    val salt = ByteArray(32)
    random.nextBytes(salt)

    return salt
}


fun generateHash(password: String): String {
    val random = SecureRandom()
    val salt = ByteArray(32)
    random.nextBytes(salt)
    val factory = SecretKeyFactory.getInstance(ALGORITHM)
    val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGHT)
    val key = factory.generateSecret(spec).encoded

    return "${Base64.getEncoder().encodeToString(salt)}:${Base64.getEncoder().encodeToString(key)}"
}

fun validatePassword(password: String, storedHashedPassword: String): Boolean {
    val salt = Base64.getUrlDecoder().decode(getSalt(storedHashedPassword))
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

private fun slowEquals(salt: ByteArray, hash: ByteArray): Boolean {
    var diff = salt.size xor hash.size
    var i = 0
    while (i < salt.size && i < hash.size) {
        diff = diff or (salt[i].toInt() xor hash[i].toInt())
        i++
    }
    return diff == 0
}






