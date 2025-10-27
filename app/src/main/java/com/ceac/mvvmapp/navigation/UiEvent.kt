package com.ceac.mvvmapp.navigation

/**
 * ----------------------------------------------------------------------------
 * UiEvent.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Esta sealed class define los **eventos de UI** que un `ViewModel` puede emitir
 * hacia la capa de presentación (UI). Es una forma elegante y segura de comunicar
 * intenciones sin acoplar la lógica de negocio con la interfaz.
 *
 * 🔹 Contexto arquitectónico:
 * - Pertenece a la capa **presentation** (entre ViewModel y la UI Compose).
 * - Facilita la comunicación “reactiva” (unidireccional):
 *      ViewModel → UiEvent → UI (HandleNavigationEvents)
 *
 * 🔹 Por qué usar una sealed class:
 * - Permite definir todos los tipos posibles de eventos en un solo lugar.
 * - Garantiza que cada tipo se maneje en un `when` exhaustivo.
 *
 * 🔹 Tipos de eventos definidos:
 * - `Navigate` → Navegar a una ruta.
 * - `NavigateBack` → Volver al destino anterior.
 * - `ShowSnackbar` → Mostrar un mensaje temporal (Toast/Snackbar).
 *
 * 🔹 Ejemplo de uso en ViewModel:
 * ```kotlin
 * _events.send(UiEvent.Navigate(Route.Home.route))
 * _events.send(UiEvent.ShowSnackbar("Inicio de sesión fallido"))
 * ```
 * ----------------------------------------------------------------------------
 */
sealed class UiEvent {

    /** 🚀 Evento para navegar hacia una ruta específica */
    data class Navigate(val route: String) : UiEvent()

    /** ⬅️ Evento para retroceder en la pila de navegación */
    data object NavigateBack : UiEvent()

    /** 💬 Evento para mostrar mensajes tipo snackbar o toast */
    data class ShowSnackbar(val message: String) : UiEvent()
}
