package com.ceac.mvvmapp.domain.usecase

import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import jakarta.inject.Inject


class LoginUseCase @Inject constructor(
    private val repo: AuthRepository // ← del dominio
) {

}
