package com.example.usermanagement.service.user

import com.example.usermanagement.model.User
import com.example.usermanagement.repository.UserRepository
import com.example.usermanagement.service.model.PaginatedResponseDTO
import com.example.usermanagement.model.UserDTO
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun deleteUserById(id: Long): Boolean {
        val existingUser = userRepository.findById(id).orElse(null)
        if (existingUser != null) {
            userRepository.delete(existingUser)
        } else {
            return false
        }
        return true
    }

    fun listAllUsers(page: Int, size: Int): PaginatedResponseDTO<UserDTO> {
        val pageable: Pageable = PageRequest.of(page, size)
        val usersPage = userRepository.findAll(pageable)

        val userDTOs = usersPage.content.map {
            UserDTO(
                id = it.id,
                username = it.username,
                email = it.email,
                role = it.role
            )
        }

        return PaginatedResponseDTO(
            page = usersPage.number,
            pageSize = usersPage.size,
            totalPages = usersPage.totalPages,
            totalElements = usersPage.totalElements,
            result = userDTOs
        )
    }
}