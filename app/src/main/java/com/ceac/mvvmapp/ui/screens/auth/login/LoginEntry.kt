package com.ceac.mvvmapp.ui.screens.auth.login

import androidx.compose.runtime.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.layout.PaddingValues
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ceac.mvvmapp.navigation.HandleNavigationEvents

/**
 * Paso 14: Entry point de la pantalla de Login (conexión VM ↔ UI).
 *
 * Explicación:
 * Este composable es el punto de entrada de la pantalla de autenticación.
 * Actúa como “container” o intermediario entre la lógica del ViewModel y la UI
 * declarativa (`LoginScreen`), sin contener lógica de negocio.
 *
 * Responsabilidades:
 * - Obtener el ViewModel mediante Hilt (`hiltViewModel()`).
 * - Suscribirse al estado expuesto por el VM (correo, contraseña, loading, error).
 * - Escuchar eventos efímeros (navegación, snackbars, etc.) mediante `HandleNavigationEvents`.
 * - Pasar el estado y callbacks a la UI pura (`LoginScreen`).
 *
 * Ventajas:
 * - Desacopla la UI del framework de inyección y del ciclo de vida.
 * - Facilita testing y mantenimiento al seguir el patrón MVVM + Compose.
 *
 * @param navController Controlador de navegación (para desplazarse entre pantallas).
 * @param snackbarHostState Host global para mostrar mensajes transitorios.
 * @param contentPadding Padding superior proporcionado por el `Scaffold` padre.
 *
 * Paso siguiente:
 * - Documentar el `LoginViewModel` para detallar cómo maneja los eventos de UI
 *   (`onEmailChanged`, `onPasswordChanged`, `onLoginClick`, etc.) y comunica
 *   resultados o errores al estado y los eventos.
 * - Asegurarse de que `LoginScreen` es “stateless”, recibiendo únicamente datos
 *   y callbacks, sin dependencias directas de ViewModel.
 */
@Composable
fun LoginEntry(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    // 1) Inyección del ViewModel de la pantalla de login
    val vm: LoginViewModel = hiltViewModel()

    // 2) Manejo centralizado de efectos de UI efímeros (snackbars / navegación)
    HandleNavigationEvents(
        navController = navController,
        snackbarHostState = snackbarHostState,
        events = vm.events
    )

    // 3) Observación reactiva del estado del ViewModel
    // Si dispones de lifecycle-runtime-compose, se recomienda collectAsStateWithLifecycle()
    val state by vm.state.collectAsState()

    // 4) Renderizado de la UI declarativa (LoginScreen)
    LoginScreen(
        state = state,
        onEmailChange = vm::onEmailChanged,         // Callback para cambios de email
        onPasswordChange = vm::onPasswordChanged,   // Callback para cambios de password
        onLoginClick = vm::onLoginClick,            // Acción principal (login)
        onRegisterClick = vm::onRegisterClick,      // Ir a pantalla de registro
        onRecoverClick = vm::onRecoverClick,        // Ir a recuperación de contraseña
        contentPadding = contentPadding
    )
}
