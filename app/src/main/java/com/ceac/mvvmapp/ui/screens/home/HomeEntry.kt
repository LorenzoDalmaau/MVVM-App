package com.ceac.mvvmapp.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ceac.mvvmapp.navigation.HandleNavigationEvents

/**
 * ----------------------------------------------------------------------------
 * HomeEntry.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Punto de entrada (`Entry Point`) de la pantalla principal de la app (Home).
 *
 * Este composable actúa como **puente entre el ViewModel y la UI pura** (`HomeScreen`).
 * Su responsabilidad es conectar el estado, los eventos de navegación y las acciones
 * de usuario de forma reactiva, siguiendo el patrón MVVM + Compose.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Responsabilidades principales:
 * ----------------------------------------------------------------------------
 * 1️⃣ Obtener el `HomeViewModel` mediante Hilt → `hiltViewModel()`.
 * 2️⃣ Escuchar sus `UiEvents` (navegación, snackbars, etc.) con `HandleNavigationEvents`.
 * 3️⃣ Observar el `state` emitido por el ViewModel (vía `collectAsState()`).
 * 4️⃣ Pasar ese estado y callbacks (`onRetry`, etc.) a `HomeScreen`.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Por qué se diseña así:
 * ----------------------------------------------------------------------------
 * - **Desacopla la UI de la lógica:** la pantalla solo pinta lo que recibe.
 * - **Inyección automática:** Hilt se encarga del ciclo de vida del ViewModel.
 * - **Reactividad declarativa:** cuando el estado cambia, Compose se recompone solo.
 * - **Reutilización:** puedes probar `HomeScreen` sin ViewModel (solo con estado dummy).
 *
 * ----------------------------------------------------------------------------
 * 🔹 Parámetros:
 * ----------------------------------------------------------------------------
 * @param navController Controlador de navegación para moverse entre pantallas.
 * @param snackbarHostState Host global para mostrar mensajes transitorios.
 * @param contentPadding Padding que viene del `Scaffold` padre (por ejemplo, barras o safe areas).
 *
 * ----------------------------------------------------------------------------
 * 🔹 Flujo visual (resumen mental):
 * ----------------------------------------------------------------------------
 * ```
 * HomeEntry (ViewModel + navegación)
 *        ↓
 * HomeScreen (UI declarativa)
 *        ↓
 * Usuario → acciones → ViewModel → nuevo estado/eventos → recomposición
 * ```
 *
 * ----------------------------------------------------------------------------
 * 🔹 Ejemplo de uso dentro de la navegación:
 * ----------------------------------------------------------------------------
 * ```
 * composable(Route.Home.route) {
 *     HomeEntry(
 *         navController = navController,
 *         snackbarHostState = snackbarHostState,
 *         contentPadding = contentPadding
 *     )
 * }
 * ```
 *
 * ----------------------------------------------------------------------------
 * 🔹 Buenas prácticas reflejadas:
 * ----------------------------------------------------------------------------
 * ✅ No hay lógica de negocio en la UI.
 * ✅ Todo lo que depende de Compose está aquí (no en el ViewModel).
 * ✅ El ViewModel no sabe nada del NavController ni del contexto Android.
 * ✅ `HandleNavigationEvents` mantiene los efectos de un solo uso centralizados.
 *
 * ----------------------------------------------------------------------------
 */
@Composable
fun HomeEntry(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    // Inyección automática del ViewModel asociado a esta pantalla
    val vm: HomeViewModel = hiltViewModel()

    // Conecta los eventos de UI (navegación, snackbars, etc.)
    HandleNavigationEvents(navController, snackbarHostState, vm.events)

    // Observa el flujo de estado y convierte en Compose State
    val state by vm.state.collectAsState()

    // Pasa estado + callbacks a la UI declarativa
    HomeScreen(
        state = state,
        onRetry = vm::load // Acción de reintentar carga de datos
    )
}
