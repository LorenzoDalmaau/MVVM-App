package com.ceac.mvvmapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState

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
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    start: Route = Route.Login
) {
    NavHost(
        navController = navController,
        startDestination = start.route,
        modifier = modifier
    ) {
        // Subgrafo de autenticación
        authGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            contentPadding = contentPadding
        )

        // Subgrafo del Home (y lo que cuelgue)
        homeGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            contentPadding = contentPadding
        )
    }
}
