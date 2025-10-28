package com.ceac.mvvmapp.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ceac.mvvmapp.ui.screens.home.HomeEntry

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    composable(Route.Home.route) {
        HomeEntry(navController = navController, snackbarHostState = snackbarHostState, contentPadding = contentPadding)
    }
}
