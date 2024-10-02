package com.example.usermanagement.controller

import com.example.usermanagement.model.AuthResponseDTO
import com.example.usermanagement.model.User
import com.example.usermanagement.service.jwt.JwtService
import com.example.usermanagement.model.UserDTO
import com.example.usermanagement.service.user.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): User {
        return userService.registerUser(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): AuthResponseDTO {

        // Autenticar o usuário
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        )

        // Carregar os detalhes do usuário
        val user = userService.findByUsername(authRequest.username)?.let {
            UserDTO(
                username = it.username,
                email = it.email,
                id = it.id,
                role = it.role,

                )
        }

        // Gerar o token JWT com base no userId
        val token = jwtService.generateToken(user!!.id)

        // Retornar o token JWT junto com os dados do usuário
        return AuthResponseDTO(
            token = token,
            user = user
        )
    }
}

data class AuthRequest(val username: String, val password: String)