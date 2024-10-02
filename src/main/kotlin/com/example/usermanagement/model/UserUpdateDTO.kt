package com.example.usermanagement.model

data class UserUpdateDTO(
    val username: String,
    val email: String,
    val role: String
)