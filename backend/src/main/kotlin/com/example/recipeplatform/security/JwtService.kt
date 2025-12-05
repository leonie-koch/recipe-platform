package com.example.recipeplatform.security

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
            .setSubject(userDetails.username)
            .setIssuedAt(currentDate)
            .setExpiration(expireDate)
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.expiration.before(Date())
    }
}
