package com.example.recipe_platform.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*


@Service
class JwtService(
    @Value("\${jwt.secret.key}") private val secretKey: String,
    @Value("\${jwt.expiration}") private val jwtExpiration: Long
) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateToken(userDetails: UserDetails): String {
        val currentDate = Date()
        val expireDate = Date(currentDate.time + jwtExpiration)

        return Jwts.builder()
            .subject(userDetails.username)
            .issuedAt(currentDate)
            .expiration(expireDate)
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .subject;
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .expiration.before(Date());
    }
}