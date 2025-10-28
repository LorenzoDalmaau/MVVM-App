package com.ceac.mvvmapp.domain.usecase

import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repo: AuthRepository
){
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repo.register(email.trim(), password)
}