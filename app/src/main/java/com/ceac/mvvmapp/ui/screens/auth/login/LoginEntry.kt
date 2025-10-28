package com.ceac.mvvmapp.ui.screens.auth.login

import com.ceac.mvvmapp.ui.screens.login.LoginScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.material3.SnackbarHostState
import com.ceac.mvvmapp.navigation.HandleNavigationEvents

@Composable
fun LoginEntry(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    val vm: LoginViewModel = hiltViewModel()
    HandleNavigationEvents(navController, snackbarHostState, vm.events)
    val state by vm.state.collectAsState()

    LoginScreen(
        state = state,
        onEmailChange = vm::onEmailChange,
        onPasswordChange = vm::onPasswordChange,
        onLoginClick = vm::onLoginClick,
        onRegisterClick = vm::onRegisterClick,
        onRecoverClick = vm::onRecoverClick
    )
}
