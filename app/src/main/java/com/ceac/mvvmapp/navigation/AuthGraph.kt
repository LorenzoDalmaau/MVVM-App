package com.ceac.mvvmapp.navigation

import com.ceac.mvvmapp.ui.screens.auth.login.LoginEntry
import com.ceac.mvvmapp.ui.screens.auth.recover.RecoverPasswordEntry
import com.ceac.mvvmapp.ui.screens.auth.register.RegisterEntry
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    composable(Route.Login.route) {
        LoginEntry(navController = navController, snackbarHostState = snackbarHostState, contentPadding = contentPadding)
    }
    composable(Route.Register.route) {
        RegisterEntry(navController = navController, snackbarHostState = snackbarHostState, contentPadding = contentPadding)
    }
    composable(Route.RecoverPassword.route) {
        RecoverPasswordEntry(navController = navController, snackbarHostState = snackbarHostState, contentPadding = contentPadding)
    }
}
