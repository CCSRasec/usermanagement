package com.example.usermanagement.controller

import com.example.usermanagement.model.User
import com.example.usermanagement.service.model.PaginatedResponseDTO
import com.example.usermanagement.service.model.UserDTO
import com.example.usermanagement.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {
    companion object{
        const val DEFAULT_PAGE = "0"
        const val DEFAULT_PAGE_SIZE = "25"
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody newUser: User): User? {
        return userService.updateUser(id, newUser)
    }

    @GetMapping
    fun listUsers(
        @RequestParam(defaultValue = DEFAULT_PAGE) page: Int,
        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) size: Int
    ): PaginatedResponseDTO<UserDTO> {
        return userService.listAllUsers(page, size)
    }
}