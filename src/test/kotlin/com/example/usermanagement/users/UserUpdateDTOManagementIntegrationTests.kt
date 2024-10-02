package com.example.usermanagement.users

import com.example.usermanagement.model.AuthResponseDTO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserUpdateDTOManagementIntegrationTests(
    @Autowired val restTemplate: TestRestTemplate
) {
    companion object {
        var jwtToken: String? = null
        var userId: Long? = null
    }

    // 1. Criar um usuário
    @Test
    @Order(1)
    fun `create new user`() {
        val requestBody = mapOf(
            "username" to "testuser",
            "password" to "password",
            "email" to "testuser@example.com"
        )
        val headers = HttpHeaders()

        val entity = HttpEntity(requestBody, headers)
        val response = restTemplate.exchange("/api/auth/register", HttpMethod.POST, entity, String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    // 2. Realizar login com o usuário criado e armazenar o JWT e ID do usuário
    @Test
    @Order(2)
    fun `login with created user and store JWT and userId`() {
        val loginBody = mapOf("username" to "testuser", "password" to "password")
        val headers = HttpHeaders()

        val entity = HttpEntity(loginBody, headers)
        val response = restTemplate.exchange("/api/auth/login", HttpMethod.POST, entity, AuthResponseDTO::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        // Armazenar o JWT e o ID do usuário
        jwtToken = response.body!!.token
        userId = response.body!!.user.id

        assertNotNull(jwtToken, "JWT token should not be null")
        assertNotNull(userId, "User ID should not be null")
    }

    // 3. Listar usuários com o JWT
    @Test
    @Order(3)
    fun `list users using JWT`() {
        assertNotNull(jwtToken, "JWT token should be available for user listing")

        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $jwtToken")

        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange("/api/users?page=0&size=5", HttpMethod.GET, entity, String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body!!.contains("testuser"))  // Verificar se o usuário está presente na listagem
    }

    // 4. Atualizar o usuário com o JWT e ID do usuário
    @Test
    @Order(4)
    fun `update existing user using JWT and userId`() {
        assertNotNull(jwtToken, "JWT token should be available for updating user")
        assertNotNull(userId, "User ID should be available for updating user")

        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $jwtToken")

        val updatedUser = mapOf(
            "username" to "updateduser",
            "email" to "updateduser@example.com",
            "role" to "ADMIN"  // Agora o role também pode ser atualizado
        )

        val entity = HttpEntity(updatedUser, headers)
        val response = restTemplate.exchange("/api/users/$userId", HttpMethod.PUT, entity, String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body!!.contains("updateduser"))  // Verificar se o nome foi atualizado
    }

    // 5. Deletar o usuário com o JWT e ID do usuário
    @Test
    @Order(5)
    fun `delete user by id using JWT and userId`() {
        assertNotNull(jwtToken, "JWT token should be available for deleting user")
        assertNotNull(userId, "User ID should be available for deleting user")

        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $jwtToken")

        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange("/api/users/$userId", HttpMethod.DELETE, entity, String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
    }
}
