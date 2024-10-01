package com.example.usermanagement

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserManagementIntegrationTests(
    @Autowired val restTemplate: TestRestTemplate
) {

    @Test
    fun `register new user`() {
        val requestBody = mapOf("username" to "testuser", "password" to "password", "email" to "test@example.com")
        val headers = HttpHeaders()

        val entity = HttpEntity(requestBody, headers)
        val response = restTemplate.exchange("/api/auth/register", HttpMethod.POST, entity, String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
    }
}