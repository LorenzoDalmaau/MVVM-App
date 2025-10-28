package com.ceac.mvvmapp.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * ----------------------------------------------------------------------------
 * Route.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Define todas las **rutas de navegación tipadas** de la app.
 * Cada pantalla se representa como un `data object` dentro de una `sealed class`.
 *
 * 🔹 Por qué usar este patrón:
 * - Evita errores tipográficos (sin strings mágicos).
 * - Facilita la autocompletación.
 * - Centraliza las rutas para toda la app.
 * - Permite añadir parámetros con `navArgument`.
 *
 * 🔹 Contexto arquitectónico:
 * - Pertenece a la **capa de navegación (UI layer)**.
 * - Se utiliza junto con `AppNav.kt` y `HandleNavigationEvents.kt`.
 *
 * 🔹 Ejemplo de uso:
 * ```kotlin
 * navController.navigate(Route.Home.route)
 * ```
 * ----------------------------------------------------------------------------
 */
sealed class Route(val route: String) {

    /** 🔐 Pantalla de inicio de sesión */
    data object Login : Route("login")

    /** 📝 Pantalla de registro */
    data object Register : Route("register")

    /** 🔑 Pantalla de recuperación de contraseña */
    data object RecoverPassword : Route("recover_password")

    /** 🏠 Pantalla principal (Home) */
    data object Home : Route("home")

    /** 🗺️ Pantalla del mapa (ejemplo de futura feature) */
    data object Map : Route("map")

    // ------------------------------------------------------------------------
    // 💡 Ejemplo avanzado: Ruta con argumentos
    // ------------------------------------------------------------------------
    // data object UserDetail : Route("user/{userId}") {
    //     val args = listOf(navArgument("userId") { type = NavType.IntType })
    //     fun build(userId: Int) = "user/$userId"
    // }
}
