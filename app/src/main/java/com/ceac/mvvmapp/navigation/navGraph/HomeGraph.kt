package com.ceac.mvvmapp.navigation.navGraph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ceac.mvvmapp.navigation.Route
import com.ceac.mvvmapp.ui.screens.home.HomeEntry

/**
 * ----------------------------------------------------------------------------
 * HomeGraph.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Este archivo define el **grafo de navegación** correspondiente al módulo **Home**,
 * que agrupa todas las pantallas disponibles una vez el usuario ha iniciado sesión.
 *
 * En este ejemplo, el grafo contiene únicamente la pantalla principal (`HomeScreen`),
 * pero puede escalar fácilmente para incluir otras rutas como:
 * - Detalle de producto
 * - Perfil de usuario
 * - Ajustes, etc.
 *
 * 🔹 Contexto arquitectónico:
 * - Pertenece a la **capa de navegación (navigation layer)** del proyecto.
 * - Forma parte de la arquitectura **modular por grafos**, donde cada "feature"
 *   (auth, home, settings...) tiene su propio `NavGraph`.
 * - Esta separación mejora la **escalabilidad** y **mantenibilidad** del código,
 *   siguiendo el principio de **Single Activity + Multiple NavGraphs**.
 *
 * 🔹 Responsabilidad:
 * - Declarar las rutas del flujo “Home”.
 * - Asociar cada ruta con su correspondiente *entry point* (`HomeEntry`),
 *   el cual se encarga de inyectar ViewModels y renderizar la UI.
 *
 * 🔹 Parámetros:
 * @param navController       Controlador de navegación que gestiona el back stack.
 * @param snackbarHostState   Permite mostrar mensajes globales (errores, avisos, etc.).
 * @param contentPadding      Padding aplicado por el layout superior (Scaffold, etc.).
 *
 * 🔹 Flujo general:
 * ```
 * MainNavHost
 *   └── homeGraph()
 *         └── Route.Home → HomeEntry()
 * ```
 *
 * 🔹 Próximos pasos:
 * - Añadir más destinos (detalle, perfil, carrito...).
 * - Aplicar animaciones entre pantallas con `AnimatedNavHost`.
 * - Implementar un sistema de deep links si la app los necesita.
 * ----------------------------------------------------------------------------
 */
fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    // 🏠 Pantalla principal (Home)
    composable(Route.Home.route) {
        HomeEntry(
            navController = navController,
            snackbarHostState = snackbarHostState,
            contentPadding = contentPadding
        )
    }
}
