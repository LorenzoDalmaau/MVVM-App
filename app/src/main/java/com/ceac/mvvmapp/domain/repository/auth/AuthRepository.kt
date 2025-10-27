package com.ceac.mvvmapp.domain.repository.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
}