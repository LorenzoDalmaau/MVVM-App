package com.ceac.mvvmapp.navigation

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow

/**
 * ----------------------------------------------------------------------------
 * HandleNavigationEvents.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Este composable se encarga de **escuchar los eventos de navegación o UI**
 * emitidos por los `ViewModel` y ejecutar las acciones correspondientes
 * sobre el `NavController`.
 *
 * Con esto conseguimos que los `ViewModel` **no conozcan directamente**
 * la navegación (ni usen NavController), manteniendo la arquitectura **limpia y desacoplada**.
 *
 * 🔹 Contexto arquitectónico:
 * - Pertenece a la capa **presentation / navigation**.
 * - Recibe un `Flow<UiEvent>` (normalmente del ViewModel) y reacciona ante él.
 *
 * 🔹 Por qué es importante:
 * Sin esta capa, los ViewModels deberían tener acceso al NavController,
 * lo cual **rompe el principio de separación de responsabilidades**.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Uso típico:
 * ----------------------------------------------------------------------------
 * ```kotlin
 * val vm: LoginViewModel = hiltViewModel()
 * HandleNavigationEvents(navController, vm.events)
 * ```
 *
 * Cuando el ViewModel emite, por ejemplo:
 * `_events.send(UiEvent.Navigate(Route.Home.route))`
 *
 * El `HandleNavigationEvents` detectará ese evento y navegará automáticamente.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Extensiones posibles:
 * ----------------------------------------------------------------------------
 * - Mostrar `Snackbar` en lugar de `Toast`.
 * - Implementar una cola de eventos más compleja.
 * - Añadir navegación condicional (por permisos, sesiones, etc.)
 *
 * ----------------------------------------------------------------------------
 */
@Composable
fun HandleNavigationEvents(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    events: Flow<UiEvent>
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navController.navigate(event.route) {
                        launchSingleTop = event.singleTop
                        event.popUpTo?.let { target ->
                            popUpTo(target) { inclusive = event.inclusive }
                        }
                    }
                }

                is UiEvent.NavigateBack -> navController.popBackStack()
                is UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

