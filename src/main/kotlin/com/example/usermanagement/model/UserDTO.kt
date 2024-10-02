package com.example.usermanagement.model


data class UserDTO(
    val id: Long,
    val username: String,
    val email: String,
    val role: String
)
