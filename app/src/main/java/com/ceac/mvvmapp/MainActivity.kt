package com.ceac.mvvmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ceac.mvvmapp.navigation.AppNav
import com.ceac.mvvmapp.navigation.HandleNavigationEvents
import com.ceac.mvvmapp.ui.screens.auth.login.LoginViewModel
import com.ceac.mvvmapp.ui.screens.login.LoginScreen
import com.ceac.mvvmapp.ui.theme.MVVMAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * ----------------------------------------------------------------------------
 * MainActivity.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Punto de entrada de la aplicación. Monta el **árbol de Compose**,
 * inicializa el **NavController** y aplica el **tema global**.
 *
 * 🔹 Decisiones de arquitectura:
 * - La navegación se delega a `AppNav`, que construye el grafo (NavHost).
 * - La Activity no contiene lógica de negocio: solo **orquesta** y **compone**.
 * - Los eventos de navegación se manejan vía `HandleNavigationEvents`, que escucha
 *   el `Flow<UiEvent>` del ViewModel (desacoplando VM de NavController).
 *
 * 🔹 Flujo (para Login):
 * 1) Inyectamos `LoginViewModel` con Hilt.
 * 2) Observamos su `state` (StateFlow).
 * 3) Pasamos callbacks del VM a `LoginScreen`.
 * 4) `HandleNavigationEvents` traduce `UiEvent` → acciones de nav.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Nota profesional:
 * Si añades `lifecycle-runtime-compose`, cambia `collectAsState()` por
 * `collectAsStateWithLifecycle()` para un consumo lifecycle-aware.
 * ----------------------------------------------------------------------------
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // 🧭 Controlador de navegación (scope de composición)
            val navController = rememberNavController()

            // 🎨 Tema global (colors, typography, shapes, spacing, elevations)
            MVVMAppTheme {
                AppNav(
                    navController = navController,
                    // 🔐 Destino: Login
                    loginScreen = {
                        // Inyecta el ViewModel con Hilt (scope del destino en NavHost)
                        val vm: LoginViewModel = hiltViewModel()

                        // Escucha eventos de navegación / UI (Navigate, NavigateBack, Snackbar…)
                        HandleNavigationEvents(navController, vm.events)

                        // Estado de la pantalla (reactivo, inmutable para la UI)
                        val state by vm.state.collectAsState()
                        // 💡 Si tienes lifecycle-runtime-compose:
                        // val state by vm.state.collectAsStateWithLifecycle()

                        // UI desacoplada: la pantalla solo “pinta” el estado y llama callbacks
                        LoginScreen(
                            state = state,
                            onEmailChange = vm::onEmailChange,
                            onPasswordChange = vm::onPasswordChange,
                            onLoginClick = vm::onLoginClick,
                            onRegisterClick = vm::onRegisterClick,
                            onRecoverClick = vm::onRecoverClick
                        )
                    },
                    // 🧾 Resto de destinos fuera del scope de esta rama → stubs temporales
                    registerScreen = {
                        val vm: com.ceac.mvvmapp.ui.screens.auth.register.RegisterViewModel = hiltViewModel()
                        HandleNavigationEvents(navController, vm.events)
                        val state by vm.state.collectAsState()
                        com.ceac.mvvmapp.ui.screens.auth.register.RegisterScreen(
                            state = state,
                            onEmailChange = vm::onEmailChange,
                            onPasswordChange = vm::onPasswordChange,
                            onRepeatChange = vm::onRepeatChange,
                            onRegisterClick = vm::onRegisterClick,
                            onBackClick = vm::onBackClick
                        )
                    },
                    recoverPasswordScreen = {
                        val vm: com.ceac.mvvmapp.ui.screens.auth.recover.RecoverPasswordViewModel = hiltViewModel()
                        HandleNavigationEvents(navController, vm.events)
                        val state by vm.state.collectAsState()
                        com.ceac.mvvmapp.ui.screens.auth.recover.RecoverPasswordScreen(
                            state = state,
                            onEmailChange = vm::onEmailChange,
                            onRecoverClick = vm::onRecoverClick,
                            onBackClick = vm::onBackClick
                        )
                    },
                    homeScreen = {
                        val vm: com.ceac.mvvmapp.ui.screens.home.HomeViewModel = hiltViewModel()
                        val state by vm.state.collectAsState()
                        com.ceac.mvvmapp.ui.screens.home.HomeScreen(
                            state = state,
                            onRetry = vm::load
                        )
                    }

                )
            }
        }
    }
}
