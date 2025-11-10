package com.ceac.mvvmapp.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ceac.mvvmapp.navigation.HandleNavigationEvents

/**
 * Paso 12: Entry point de Home (contenedor que conecta VM ↔ UI).
 *
 * Explicación:
 * Este composable actúa como el "container" de la pantalla Home.
 * Su responsabilidad es:
 * - Obtener el ViewModel con Hilt: [hiltViewModel].
 * - Suscribirse a su estado ([HomeViewModel.state]) y convertirlo a State para Compose.
 * - Escuchar eventos efímeros ([HomeViewModel.events]) y delegarlos a un manejador
 *   centralizado de efectos de UI ([HandleNavigationEvents]) para navegación y snackbars.
 * - Pasar a la UI "stateless" ([HomeScreen]) el estado y los callbacks necesarios.
 *
 * Ventajas:
 * - La UI permanece declarativa y sin dependencias de inyección ni lógica de negocio.
 * - La navegación y los efectos efímeros quedan centralizados fuera del ViewModel.
 *
 * @param navController Controlador de navegación que gestiona el backstack de Compose.
 * @param snackbarHostState Host global para mostrar snackbars desde eventos de UI.
 * @param contentPadding Padding externo proporcionado por un Scaffold/host superior.
 *
 * Paso siguiente:
 * Implementar/ajustar [HomeScreen] como UI "stateless" que reciba:
 * - `state: HomeUiState`
 * - callbacks como `onRetry` y, opcionalmente, `onItemClick`
 * y aplique `contentPadding` mediante `Modifier.padding(contentPadding)`.
 */
@Composable
fun HomeEntry(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    // 1) ViewModel con scope del destino en el NavHost (inyectado por Hilt)
    val vm: HomeViewModel = hiltViewModel()

    // 2) Manejo de eventos efímeros (navegación, snackbars, back) en un único lugar
    HandleNavigationEvents(
        navController = navController,
        snackbarHostState = snackbarHostState,
        events = vm.events
    )

    // 3) Observación del estado como Compose State (recomposición automática)
    //    Nota: si dispones de lifecycle-runtime-compose, puedes usar collectAsStateWithLifecycle()
    val state by vm.state.collectAsState()

    // 4) Render de la UI declarativa (stateless) con datos y callbacks
    HomeScreen(
        state = state,
        onRetry = vm::load,
        contentPadding = contentPadding
    )
}
