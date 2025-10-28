package com.ceac.mvvmapp.domain.usecase

import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import javax.inject.Inject

/// TODO Añadir comentarios

class RecoverPasswordUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> = repo.recoverPassword(email.trim())
}