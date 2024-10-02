package com.example.usermanagement.service.model


data class UserDTO(
    val id: Long,
    val username: String,
    val email: String,
    val role: String
)
