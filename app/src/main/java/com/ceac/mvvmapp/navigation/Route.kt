package com.ceac.mvvmapp.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * ----------------------------------------------------------------------------
 * Route.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Este archivo define todas las **rutas de navegación** de la aplicación.
 * Cada pantalla se representa como un `data object` dentro de la sealed class `Route`,
 * lo que permite tener una navegación **tipada, mantenible y escalable**.
 *
 * 🔹 Contexto arquitectónico:
 * - Pertenece a la capa **navigation**, dentro de la presentación (UI layer).
 * - Se utiliza junto con `NavHost` (AppNav.kt) y `NavController` para definir
 *   y controlar las pantallas accesibles en la app.
 *
 * 🔹 Por qué usar una sealed class:
 * - Evita errores por escribir mal los nombres de las rutas (sin strings sueltos).
 * - Permite extender fácilmente nuevas pantallas.
 * - Centraliza todas las rutas de la app en un único punto de referencia.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Ejemplo de uso:
 * ----------------------------------------------------------------------------
 * ```kotlin
 * navController.navigate(Route.Home.route)
 * ```
 *
 * ----------------------------------------------------------------------------
 * 🔹 Extensiones posibles:
 * ----------------------------------------------------------------------------
 * - Añadir rutas con parámetros (por ejemplo, “user/{userId}”).
 * - Incluir listas de argumentos esperados con `navArgument()`.
 * ----------------------------------------------------------------------------
 */
sealed class Route(val route: String) {

    /** 🧑‍💻 Pantalla de inicio de sesión */
    data object Login : Route("login")

    /** 📝 Pantalla de registro de usuario */
    data object Register : Route("register")

    /** 🔑 Pantalla para recuperar la contraseña */
    data object RecoverPassword : Route("recover_password")

    /** 🏠 Pantalla principal tras iniciar sesión */
    data object Home : Route("home")

    // ------------------------------------------------------------------------
    // Ejemplo comentado: Ruta con argumentos
    // ------------------------------------------------------------------------
    // En caso de necesitar navegación con parámetros (por ejemplo, un ID de usuario)
    // puedes usar el siguiente patrón:
    //
    // data object UserDetail : Route("user/{userId}") {
    //     // Definimos los argumentos esperados
    //     val args = listOf(navArgument("userId") { type = NavType.IntType })
    //
    //     // Función helper para construir la ruta con el parámetro
    //     fun build(userId: Int) = "user/$userId"
    // }
}
