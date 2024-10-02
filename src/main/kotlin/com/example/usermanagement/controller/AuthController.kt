package com.example.usermanagement.controller

import com.example.usermanagement.model.User
import com.example.usermanagement.service.jwt.JwtService
import com.example.usermanagement.service.user.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
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
    fun login(@RequestBody authRequest: AuthRequest): String {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        )
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(authRequest.username)
        return jwtService.generateToken(userDetails.username)
    }
}

data class AuthRequest(val username: String, val password: String)