package com.ceac.mvvmapp.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceac.mvvmapp.domain.usecase.auth.LoginUseCase
import com.ceac.mvvmapp.navigation.Route
import com.ceac.mvvmapp.navigation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Paso 15: ViewModel de la pantalla de Login.
 *
 * Explicación:
 * Este ViewModel implementa la capa de **lógica de presentación** de la pantalla de login.
 * Gestiona el estado reactivo de la UI y coordina la interacción entre la vista
 * (`LoginScreen`) y la capa de dominio (`LoginUseCase`).
 *
 * Su papel dentro de MVVM:
 * - Recibe eventos del usuario desde la UI (por ejemplo, al escribir o pulsar "Iniciar sesión").
 * - Actualiza el estado interno (`UiState`) de manera reactiva e inmutable.
 * - Llama al caso de uso `LoginUseCase` para realizar la autenticación.
 * - Emite `UiEvent` de un solo uso (como navegación o mostrar snackbar) a la capa de UI.
 *
 * Ventajas:
 * - Separa claramente la lógica de negocio de la representación visual.
 * - Evita efectos secundarios en Compose al usar `StateFlow` y `Channel` para comunicación unidireccional.
 * - Facilita el testeo y la reutilización de la lógica de login.
 *
 * Dependencias:
 * - `LoginUseCase`: caso de uso del dominio responsable de autenticar al usuario y
 *   persistir los tokens mediante el repositorio de autenticación.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    /**
     * Representa el estado inmutable de la UI de Login.
     *
     * Propiedades:
     * @param email Campo de correo electrónico actual.
     * @param password Contraseña actual.
     * @param isLoading Indica si se está procesando una autenticación.
     * @param error Mensaje de error mostrado en caso de fallo.
     */
    data class UiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val error: String? = null
    )

    /** Estado observable de la pantalla (única fuente de verdad para la UI). */
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    /** Canal para eventos efímeros de UI (navegación, snackbars, etc.). */
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    /**
     * Paso 15.1: Actualiza el campo email en el estado actual.
     * @param v Nuevo valor introducido por el usuario.
     */
    fun onEmailChanged(v: String) =
        _state.update { it.copy(email = v, error = null) }

    /**
     * Paso 15.2: Actualiza el campo password en el estado actual.
     * @param v Nuevo valor introducido por el usuario.
     */
    fun onPasswordChanged(v: String) =
        _state.update { it.copy(password = v, error = null) }

    /**
     * Paso 15.3: Ejecuta la acción de login.
     *
     * Flujo de ejecución:
     * 1) Cambia el estado a `isLoading = true`.
     * 2) Llama al caso de uso `LoginUseCase` con las credenciales actuales.
     * 3) Según el resultado:
     *    - Éxito → navega a [Route.Home].
     *    - Error → muestra el mensaje en el estado.
     * 4) Restaura `isLoading = false` al finalizar.
     */
    fun onLoginClick() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }

        val s = state.value
        val result = loginUseCase(s.email, s.password)

        result.fold(
            onSuccess = {
                _events.send(
                    UiEvent.Navigate(
                        route = Route.Home.route,
                        popUpTo = Route.Login.route,
                        inclusive = true
                    )
                )
            },
            onFailure = { ex ->
                _state.update { it.copy(error = ex.message ?: "Credenciales inválidas") }
            }
        )

        _state.update { it.copy(isLoading = false) }
    }

    /**
     * Paso 15.4: Navega hacia la pantalla de registro.
     */
    fun onRegisterClick() = viewModelScope.launch {
        _events.send(UiEvent.Navigate(Route.Register.route))
    }

    /**
     * Paso 15.5: Navega hacia la pantalla de recuperación de contraseña.
     */
    fun onRecoverClick() = viewModelScope.launch {
        _events.send(UiEvent.Navigate(Route.RecoverPassword.route))
    }
}
