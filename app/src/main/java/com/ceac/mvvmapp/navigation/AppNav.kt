package com.ceac.mvvmapp.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.flow.Flow

/**
 * ----------------------------------------------------------------------------
 * AppNav.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Composable raíz que **configura el grafo de navegación** de la app usando
 * Navigation-Compose. Define las rutas disponibles y conecta cada destino
 * con su pantalla correspondiente.
 *
 * 🔹 Por qué así (decisión de diseño):
 * - Recibe las pantallas por **lambdas @Composable** (inyección por parámetros).
 *   Esto desacopla la navegación del código de UI y facilita **reutilización y testeo**.
 * - El **start** se pasa como `Route` para poder cambiar el destino inicial
 *   (por ejemplo, onboarding vs. login) sin tocar el grafo.
 *
 * 🔹 Uso típico:
 * Se invoca desde `MainActivity`:
 * ```
 * AppNav(
 *   navController = rememberNavController(),
 *   start = Route.Login,
 *   loginScreen = { LoginScreen(...) },
 *   registerScreen = { RegisterScreen(...) },
 *   recoverPasswordScreen = { RecoverPasswordScreen(...) },
 *   homeScreen = { HomeScreen(...) }
 * )
 * ```
 *
 * 🔹 Cómo añadir una nueva pantalla:
 * 1) Declara la ruta en `Route.kt`.
 * 2) Añade su `composable(...)` aquí dentro del `NavHost`.
 * 3) Pasa la lambda correspondiente desde `MainActivity`.
 *
 * 🔹 Parámetros en rutas (chuleta):
 * - En `Route.kt`: `data object UserDetail : Route("user/{userId}")`
 * - En `AppNav`: `composable(Route.UserDetail.route, arguments = Route.UserDetail.args) { ... }`
 * - Para navegar: `navController.navigate("user/${id}")`
 *
 * ----------------------------------------------------------------------------
 */
@Composable
fun AppNav(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    start: Route = Route.Login,
    // 🧩 Lambdas de pantallas: aquí inyectamos la UI de cada destino.
    loginScreen: @Composable () -> Unit,
    registerScreen: @Composable () -> Unit,
    recoverPasswordScreen: @Composable () -> Unit,
    homeScreen: @Composable () -> Unit,
) {
    // NavHost: contenedor del grafo de navegación.
    NavHost(
        navController = navController,
        startDestination = start.route,
        modifier = modifier
    ) {
        // Cada "composable" asocia una ruta con su contenido UI.
        composable(Route.Login.route) { loginScreen() }
        composable(Route.Register.route) { registerScreen() }
        composable(Route.RecoverPassword.route) { recoverPasswordScreen() }
        composable(Route.Home.route) { homeScreen() }

        // 🔖 Ejemplo (comentado) de destino con parámetros:
        // composable(
        //   route = Route.UserDetail.route,
        //   arguments = Route.UserDetail.args
        // ) { backStackEntry ->
        //     val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
        //     UserDetailScreen(userId = userId)
        // }
    }
}