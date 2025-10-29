package com.ceac.mvvmapp.ui.screens.auth.recover

/// TODO Añadir comentarios

data class RecoverPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val submitError: String? = null
) {
    val isValid: Boolean
        get() = emailError == null && email.isNotBlank()
}