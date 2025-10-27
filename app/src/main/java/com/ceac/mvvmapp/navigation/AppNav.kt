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

@Composable
fun HandleNavigationEvents(navController: NavHostController, events: Flow<UiEvent>) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is UiEvent.Navigate -> navController.navigate(event.route)
                is UiEvent.NavigateBack -> navController.popBackStack()
                is UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

