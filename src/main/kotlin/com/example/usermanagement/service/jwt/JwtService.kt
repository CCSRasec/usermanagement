package com.example.usermanagement.service.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {

    private val SECRET_KEY = "mysecretkey"

    fun generateToken(userId: Long): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, userId.toString())
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validade
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY.toByteArray())
            .compact()
    }

    fun validateToken(token: String): Long? {
        try {
            val claims: Claims = extractAllClaims(token)
            return claims.subject.toLong()
        } catch (e: Exception) {
            return null
        }
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY.toByteArray())
            .parseClaimsJws(token)
            .body
    }
}