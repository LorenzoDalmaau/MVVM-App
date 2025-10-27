package com.ceac.mvvmapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNav(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    start: Route = Route.Login,
    loginScreen: @Composable () -> Unit,
    registerScreen: @Composable () -> Unit,
    recoverPasswordScreen: @Composable () -> Unit,
    homeScreen: @Composable () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = start.route,
        modifier = modifier
    ) {
        composable(Route.Login.route) { loginScreen() }
        composable(Route.Register.route) { registerScreen() }
        composable(Route.RecoverPassword.route) { recoverPasswordScreen() }
        composable(Route.Home.route) { homeScreen() }
    }
}
