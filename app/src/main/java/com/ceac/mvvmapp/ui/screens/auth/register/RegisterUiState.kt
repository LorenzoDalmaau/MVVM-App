package com.ceac.mvvmapp.ui.screens.auth.register

/// TODO Añadir comentarios

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val repeat: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val repeatError: String? = null,
    val submitError: String? = null
) {
    val isValid: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                repeat.isNotBlank() &&
                emailError == null &&
                passwordError == null &&
                repeatError == null
}
