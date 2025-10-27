package com.ceac.mvvmapp.navigation

/**
 * Representa eventos de un ViewModel hacia la UI (navegación, snackbars, etc).
 * Se usa con un Channel/Flow en el ViewModel y se escucha desde la pantalla.
 */
sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
    object NavigateBack : UiEvent()
}
