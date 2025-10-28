package com.ceac.mvvmapp.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceac.mvvmapp.domain.usecase.GetProductsUseCase
import com.ceac.mvvmapp.navigation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ----------------------------------------------------------------------------
 * HomeViewModel.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * ViewModel de la pantalla Home. Orquesta la **carga de productos** usando el
 * caso de uso `GetProductsUseCase`, gestiona el **estado de UI** (`HomeUiState`)
 * y emite **eventos efímeros** (`UiEvent`) para la capa de presentación.
 *
 * 🔹 Patrón arquitectónico:
 * - Capa: **Presentation / ViewModel** (MVVM + Clean Architecture)
 * - Flujo: UI → acciones (onRetry) → ViewModel → UseCase → Repository → (resultado)
 * - Estado unidireccional: el ViewModel **emite** estado, la UI **observa**.
 *
 * 🔹 Responsabilidades:
 * - Exponer el estado de Home (`isLoading`, `products`, `error`).
 * - Ejecutar la carga inicial y el reintento (`load()`).
 * - No conoce detalles de infraestructura (Retrofit/Room); habla con el dominio.
 *
 * 🔹 Eventos de UI:
 * - `_events` permite enviar señales puntuales (snackbar, navegación…).
 *   En este ViewModel de ejemplo no se usan aún, pero queda preparado para
 *   futuros flujos (p.ej., abrir detalle de producto).
 *
 * 🔹 SavedStateHandle (opcional):
 * - Útil si quieres **persistir filtros, scroll** o parámetros de navegación
 *   entre recreaciones de proceso/rotaciones. Si no se usa, puede eliminarse.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Extensiones recomendadas (futuro “empresa real”):
 * ----------------------------------------------------------------------------
 * - **Paginación** (Paging 3) si la lista es larga.
 * - **Retry/backoff** exponencial en errores de red.
 * - **Cache local** (Room) + política offline-first.
 * - **UI events** para feedback (snackbar “Actualizado”, etc.).
 * - **collectAsStateWithLifecycle** en UI para consumo lifecycle-aware.
 * - **Tests** unitarios del load() con repositorio fake.
 * ----------------------------------------------------------------------------
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val savedStateHandle: SavedStateHandle // ❗️Opcional: elimínalo si no lo usas
) : ViewModel() {

    // Estado observable por la UI
    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    // Eventos efímeros (navegación/snackbar). Preparado para futuras acciones.
    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        // Carga inicial al crear el ViewModel
        load()
    }

    /**
     * Carga (o recarga) la lista de productos.
     *
     * Estrategia:
     * 1) Marca loading y limpia error.
     * 2) Ejecuta el caso de uso.
     * 3) Publica el resultado en el estado:
     *    - Éxito  → lista en `products`.
     *    - Error   → mensaje en `error`.
     */
    fun load() = viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, error = null)

        runCatching { getProducts() }
            .onSuccess { list ->
                _state.value = HomeUiState(isLoading = false, products = list)
            }
            .onFailure { ex ->
                _state.value = HomeUiState(
                    isLoading = false,
                    error = ex.message ?: "Error al cargar productos"
                )
            }
    }
}
