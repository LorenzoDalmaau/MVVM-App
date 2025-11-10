package com.ceac.mvvmapp.navigation

/**
 * Paso 17: Sistema de eventos efímeros (UiEvent)
 * ----------------------------------------------------------------------------
 * Esta sealed class define los **eventos de interfaz de usuario de un solo uso**
 * que el ViewModel puede emitir hacia la capa de presentación (Compose).
 *
 * Su objetivo es resolver un problema clásico en arquitectura reactiva:
 * cómo comunicar **acciones puntuales** (navegación, snackbars, toasts, diálogos)
 * sin romper el flujo unidireccional de datos.
 *
 * A diferencia del `UiState` (que representa la pantalla en todo momento),
 * estos eventos son **efímeros**: se consumen una sola vez.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Patrón aplicado: UDF (Unidirectional Data Flow)
 * ----------------------------------------------------------------------------
 * - El ViewModel emite el evento → la UI lo recibe → se ejecuta la acción.
 * - No hay comunicación inversa ni dependencia de contexto Android.
 * - Esto mantiene el ViewModel puro y fácilmente testeable.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Tipos de eventos:
 * ----------------------------------------------------------------------------
 * 1️⃣ [Navigate] → Navegar a una nueva ruta.
 * 2️⃣ [NavigateBack] → Retroceder en el stack de navegación.
 * 3️⃣ [ShowSnackbar] → Mostrar un mensaje temporal al usuario.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Ejemplo de uso en ViewModel:
 * ----------------------------------------------------------------------------
 * ```
 * _events.send(UiEvent.Navigate(Route.Home.route))
 * _events.send(UiEvent.ShowSnackbar("Inicio de sesión fallido"))
 * ```
 *
 * ----------------------------------------------------------------------------
 * 🔹 Flujo completo de ejecución:
 * ----------------------------------------------------------------------------
 * ViewModel ──► UiEvent ──► HandleNavigationEvents ──► NavController / SnackbarHost
 *
 * ----------------------------------------------------------------------------
 * 🔹 Ventajas:
 * ----------------------------------------------------------------------------
 * ✅ Evita fugas de memoria (no guarda referencia a contextos).
 * ✅ Desacopla la navegación del ViewModel.
 * ✅ Facilita pruebas unitarias del flujo de eventos.
 * ✅ Cohesiona la comunicación UI ↔ ViewModel bajo un canal único.
 *
 * ----------------------------------------------------------------------------
 */
sealed class UiEvent {

    /** 🔹 Navegar a una nueva ruta del NavGraph */
    data class Navigate(
        val route: String,
        val popUpTo: String? = null,   // opcional: limpiar backstack hasta esta ruta
        val inclusive: Boolean = false,
        val singleTop: Boolean = true  // evita duplicar destino en el backstack
    ) : UiEvent()

    /** 🔹 Retroceder en el stack de navegación */
    data object NavigateBack : UiEvent()

    /** 🔹 Mostrar mensaje efímero (Snackbar) */
    data class ShowSnackbar(val message: String) : UiEvent()
}
