package com.ceac.mvvmapp.ui.screens.auth.register

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ceac.mvvmapp.navigation.HandleNavigationEvents

@Composable
fun RegisterEntry(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    val vm: RegisterViewModel = hiltViewModel()
    HandleNavigationEvents(navController, snackbarHostState, vm.events)
    val state by vm.state.collectAsState()

    RegisterScreen(
        state = state,
        onEmailChange = vm::onEmailChange,
        onPasswordChange = vm::onPasswordChange,
        onRepeatChange = vm::onRepeatChange,
        onRegisterClick = vm::onRegisterClick,
        onBackClick = vm::onBackClick
    )
}
