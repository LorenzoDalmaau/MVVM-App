package com.ceac.mvvmapp.ui.screens.auth.login

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceac.mvvmapp.domain.usecase.LoginUseCase
import com.ceac.mvvmapp.navigation.Route
import com.ceac.mvvmapp.navigation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onEmailChange(v: String) {
        _state.value = _state.value.copy(
            email = v,
            emailError = validateEmail(v),
            submitError = null
        )
    }

    fun onPasswordChange(v: String) {
        _state.value = _state.value.copy(
            password = v,
            passwordError = validatePassword(v),
            submitError = null
        )
    }

    fun onLoginClick() = viewModelScope.launch {
        val s = _state.value
        val eErr = validateEmail(s.email)
        val pErr = validatePassword(s.password)
        if (eErr != null || pErr != null) {
            _state.value = s.copy(emailError = eErr, passwordError = pErr)
            return@launch
        }

        _state.value = s.copy(isLoading = true, submitError = null)
        val result = loginUseCase(s.email, s.password)
        _state.value = _state.value.copy(isLoading = false)

        result.onSuccess {
            _events.send(UiEvent.Navigate(Route.Home.route))
        }.onFailure { ex ->
            _state.value = _state.value.copy(submitError = ex.message ?: "Error desconocido")
        }
    }

    fun onRegisterClick() = viewModelScope.launch {
        _events.send(UiEvent.Navigate(Route.Register.route))
    }

    fun onRecoverClick() = viewModelScope.launch {
        _events.send(UiEvent.Navigate(Route.RecoverPassword.route))
    }

    private fun validateEmail(e: String): String? =
        when {
            e.isBlank() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(e).matches() -> "Formato de email no válido"
            else -> null
        }

    private fun validatePassword(p: String): String? =
        if (p.length < 4) "Mínimo 4 caracteres" else null
}
