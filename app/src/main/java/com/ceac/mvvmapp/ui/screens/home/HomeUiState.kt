package com.ceac.mvvmapp.ui.screens.home

import com.ceac.mvvmapp.domain.model.Product

/**
 * Paso 10: Definición del estado inmutable de la pantalla Home (`HomeUiState`).
 *
 * Explicación:
 * En el patrón **MVVM con Jetpack Compose**, la interfaz de usuario (UI)
 * se representa siempre a partir de un único estado inmutable.
 * En lugar de modificar directamente los elementos de la pantalla,
 * el ViewModel actualiza una instancia de este estado,
 * y Compose se recompone automáticamente.
 *
 * Este enfoque facilita:
 * - Pruebas unitarias más sencillas (el estado es un único objeto).
 * - Depuración clara (puedes ver exactamente en qué estado estaba la UI).
 * - Eliminación de inconsistencias o “glitches” visuales.
 *
 * Arquitectura:
 * - Pertenece a la capa **presentation → ui.screens.home**.
 * - Es emitido por `HomeViewModel` como un `StateFlow<HomeUiState>`.
 * - Es observado desde `HomeEntry` o `HomeScreen` mediante `collectAsState()`.
 *
 * Flujo de uso:
 * ```
 * ViewModel → (emite) → HomeUiState → (observa) → UI
 * ```
 *
 * Paso siguiente:
 * Implementar el `HomeViewModel`, que gestionará la lógica
 * para cargar productos desde el backend usando el `GetProductsUseCase`
 * y emitirá nuevas instancias de este estado.
 */
data class HomeUiState(

    /**
     * Indica si la pantalla se encuentra actualmente cargando datos.
     * Cuando es `true`, la UI muestra un `CircularProgressIndicator`.
     */
    val isLoading: Boolean = false,

    /**
     * Lista de productos obtenidos desde el dominio o backend.
     * Si está vacía, la pantalla puede mostrar un estado inicial o un mensaje vacío.
     */
    val items: List<Product> = emptyList(),

    /**
     * Mensaje de error en caso de fallo durante la carga.
     * Si es `null`, se asume que no hay errores activos.
     */
    val error: String? = null,

    /**
     * Página actual usada en la paginación de productos.
     * Inicia en 0 y se incrementa con cada nueva carga de más datos.
     */
    val page: Int = 0,

    /**
     * Tamaño del lote de productos solicitados por cada petición.
     * Normalmente se usa en el endpoint `/products?page=x&size=y`.
     */
    val size: Int = 20,

    /**
     * Indica si se ha llegado al final de la lista (no hay más productos que cargar).
     * Esto permite optimizar la UI para no seguir pidiendo páginas vacías.
     */
    val endReached: Boolean = false
)
