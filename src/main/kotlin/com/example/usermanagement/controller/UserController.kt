package com.example.usermanagement.controller

import com.example.usermanagement.model.User
import com.example.usermanagement.service.model.UserDTO
import com.example.usermanagement.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody newUser: User): User? {
        return userService.updateUser(id, newUser)
    }

    @GetMapping
    fun listUsers(): List<UserDTO> {
        return userService.listAllUsers()
    }
}