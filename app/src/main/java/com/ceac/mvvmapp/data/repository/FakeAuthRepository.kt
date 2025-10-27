package com.ceac.mvvmapp.data.repository

import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        return if (email == "admin@ceac.com" && password == "1234") {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Credenciales incorrectas"))
        }
    }
}
