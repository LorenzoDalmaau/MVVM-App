package com.ceac.mvvmapp.ui.screens.home

import com.ceac.mvvmapp.domain.model.Product

/**
 * ----------------------------------------------------------------------------
 * HomeUiState.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Representa el **estado inmutable de la pantalla Home**.
 *
 * En el patrón **MVVM + Jetpack Compose**, la UI no pregunta “qué hacer”:
 * simplemente observa este estado, y se recompone automáticamente cada vez
 * que cambia.
 *
 * Este enfoque garantiza que la interfaz sea **reactiva, predecible y testeable**.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Propiedades:
 * ----------------------------------------------------------------------------
 * @param isLoading Indica si los datos se están cargando (mostrar spinner).
 * @param products Lista de productos ya cargados desde el dominio/repositorio.
 * @param error Mensaje de error si la carga falla; `null` si no hay error.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Ejemplo de ciclo de vida:
 * ----------------------------------------------------------------------------
 * 1️⃣ Inicial → `isLoading = true`, `products = []`, `error = null`
 * 2️⃣ Éxito → `isLoading = false`, `products = [ ... ]`, `error = null`
 * 3️⃣ Error → `isLoading = false`, `products = []`, `error = "Error al cargar"`
 *
 * En Compose, cada uno de estos estados produce una **UI distinta**:
 * - Loading → Spinner centrado
 * - Error → Mensaje + botón “Reintentar”
 * - Éxito → Lista de productos
 *
 * ----------------------------------------------------------------------------
 * 🔹 Principios aplicados:
 * ----------------------------------------------------------------------------
 * ✅ **Inmutabilidad:** Cada cambio crea una nueva instancia del estado.
 * ✅ **Unidireccionalidad:** El ViewModel emite → la UI reacciona.
 * ✅ **Simplicidad:** Una sola fuente de verdad para toda la pantalla.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Ejemplo de uso en el ViewModel:
 * ----------------------------------------------------------------------------
 * ```
 * _state.value = HomeUiState(isLoading = true)
 * val result = getProductsUseCase()
 * _state.value = HomeUiState(products = result)
 * ```
 * ----------------------------------------------------------------------------
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null
)
