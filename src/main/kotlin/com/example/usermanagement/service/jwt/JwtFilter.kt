package com.example.usermanagement.service.jwt

import com.example.usermanagement.service.user.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtService: JwtService,
    @Lazy private val userService: UserService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        var userId: Long? = null
        var jwt: String? = null

        // Verifica se o cabeçalho contém o token JWT
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7) // Remove o prefixo "Bearer "
            userId = jwtService.validateToken(jwt)  // Valida o token JWT e retorna o userId
        }

        // Se o userId foi identificado e a autenticação ainda não foi definida
        if (userId != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails: UserDetails = userService.findUserDetailsById(userId)

            // Se o token for válido, configure a autenticação manualmente
            if (jwtService.validateToken(jwt!!) == userId) {
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }
}