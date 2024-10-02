package com.example.usermanagement.service.model

data class PaginatedResponseDTO<T>(
    val page: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalElements: Long,
    val result: List<T>
)
