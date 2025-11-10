package com.ceac.mvvmapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ceac.mvvmapp.domain.usecase.GetProductsUseCase
import com.ceac.mvvmapp.navigation.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Paso 11: Orquestación de estado y eventos de la pantalla Home.
 *
 * Explicación:
 * Este ViewModel actúa como el “cerebro” de la pantalla Home. Se encarga de:
 * - Gestionar el estado inmutable [HomeUiState] expuesto como [StateFlow].
 * - Disparar la carga de productos mediante el caso de uso [GetProductsUseCase].
 * - Emitir eventos de UI de un solo uso ([UiEvent]) como navegación o snackbars.
 *
 * Arquitectura y responsabilidades:
 * - Capa de presentación (MVVM). No conoce detalles de red ni de frameworks de UI.
 * - Orquesta el flujo: UI → acción de usuario → ViewModel → UseCase → Repository → DTO → Dominio → nuevo estado → UI.
 *
 * Consideraciones:
 * - El estado se actualiza con `_state.update { it.copy(...) }` para mantener inmutabilidad y claridad.
 * - Los efectos de un solo uso se emiten por `Channel` → `Flow` para evitar duplicidades tras recomposición.
 *
 * Paso siguiente:
 * Conectar este ViewModel a la UI declarativa mediante un “Entry” que observe `state` y escuche `events`,
 * y una `HomeScreen` stateless que reciba solo datos y callbacks.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    /** Caso de uso de dominio para obtener productos paginados. */
    private val getProducts: GetProductsUseCase
) : ViewModel() {

    // -------------------------------------------------------------------------
    // Estado observable (UI State)
    // -------------------------------------------------------------------------

    /** Flujo interno mutable que contiene el estado actual de la pantalla. */
    private val _state = MutableStateFlow(HomeUiState())

    /**
     * Flujo público inmutable para que la UI observe el estado.
     * Compose lo consumirá típicamente con `collectAsState()`.
     */
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    // -------------------------------------------------------------------------
    // Eventos de UI (one-shot)
    // -------------------------------------------------------------------------

    /** Canal para efectos de un solo uso: navegación, snackbars, etc. */
    private val _events = Channel<UiEvent>(Channel.BUFFERED)

    /** Flujo de solo lectura de eventos para la UI. */
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    // -------------------------------------------------------------------------
    // Inicialización
    // -------------------------------------------------------------------------

    init {
        // Carga inicial de la primera página nada más crear el VM.
        load()
    }

    // -------------------------------------------------------------------------
    // Acciones públicas
    // -------------------------------------------------------------------------

    /**
     * Carga o recarga la lista de productos desde el backend usando el caso de uso.
     *
     * Flujo de ejecución:
     * 1) Prevención de concurrencia: si ya está cargando o se alcanzó el final, retorna.
     * 2) Marca `isLoading = true` y resetea `error`.
     * 3) Invoca `getProducts(page, size)` que devuelve `Result<List<Product>>`.
     * 4) En `onSuccess`: concatena resultados (si `page > 0`) y marca `endReached` si la lista recibida es menor que `size`.
     * 5) En `onFailure`: setea `error` y emite un `UiEvent.ShowSnackbar` informativo.
     *
     * @param page Número de página a cargar. Por defecto, usa el del estado actual.
     * @param size Tamaño de página. Por defecto, usa el del estado actual.
     */
    fun load(
        page: Int = state.value.page,
        size: Int = state.value.size
    ) = viewModelScope.launch {
        // Evita cargas simultáneas o seguir pidiendo si ya no hay más resultados
        if (_state.value.isLoading || _state.value.endReached) return@launch

        // 1) Indicador de carga y limpieza de error previo
        _state.update { it.copy(isLoading = true, error = null) }

        // 2) Llamada al caso de uso (dominio)
        val result = getProducts(page, size)

        // 3) Interpretación del resultado
        result.fold(
            onSuccess = { list ->
                val reachedEnd = list.size < size
                _state.update {
                    it.copy(
                        isLoading = false,
                        items = if (page == 0) list else it.items + list,
                        page = page,
                        size = size,
                        endReached = reachedEnd,
                        error = null
                    )
                }
            },
            onFailure = { ex ->
                _state.update { it.copy(isLoading = false, error = ex.message ?: "Error de carga") }
                _events.send(UiEvent.ShowSnackbar(ex.message ?: "No se pudieron cargar los productos"))
            }
        )
    }

    /**
     * Solicita una navegación a la pantalla de detalle del producto.
     *
     * Nota:
     * - El ViewModel no conoce NavController. Emite un [UiEvent] que será interpretado por la capa de UI.
     *
     * @param id Identificador del producto al que se desea navegar.
     */
    fun navigateToDetail(id: String) = viewModelScope.launch {
        _events.send(UiEvent.Navigate(route = "detail/$id"))
    }
}
