package com.example.usermanagement.service.user

import com.example.usermanagement.model.User
import com.example.usermanagement.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun registerUser(user: User): User {
        val encryptedPassword = passwordEncoder.encode(user.password)
        val newUser = user.copy(password = encryptedPassword)
        return userRepository.save(newUser)
    }

    fun updateUser(id: Long, newUser: User): User? {
        val existingUser = userRepository.findById(id).orElse(null)
        return if (existingUser != null) {
            val updatedUser = existingUser.copy(
                username = newUser.username,
                email = newUser.email
            )
            userRepository.save(updatedUser)
        } else {
            null
        }
    }

    fun listAllUsers(): List<User> = userRepository.findAll()
}