package com.example.usermanagement.service.user

import com.example.usermanagement.model.User
import com.example.usermanagement.model.UserUpdateDTO
import com.example.usermanagement.repository.UserRepository
import com.example.usermanagement.service.model.PaginatedResponseDTO
import com.example.usermanagement.model.UserDTO
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
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

    fun updateUser(id: Long, newUserUpdateDTO: UserUpdateDTO): UserUpdateDTO? {
        val existingUser = userRepository.findById(id).orElse(null)
        return if (existingUser != null) {
            val updatedUser = existingUser.copy(
                username = newUserUpdateDTO.username,
                email = newUserUpdateDTO.email
            )
            userRepository.save(updatedUser).run {
                UserUpdateDTO(username = this.username, email = this.email, role = this.role)
            }
        } else {
            null
        }
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun findUserDetailsById(id: Long): UserDetails {
        val user: User = userRepository.findById(id).orElseThrow { RuntimeException("User not found") }
        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            emptyList()  // Carregar as autoridades do usuário, se necessário
        )
    }

    fun findById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
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