package com.example.usermanagement.service.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {
    private val SECRET_KEY = "mysecretkey"

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 1 hora de validade
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

    fun validateToken(token: String): String? {
        return try {
            val claims: Claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body
            claims.subject
        } catch (e: Exception) {
            null
        }
    }
}