package com.ceac.mvvmapp.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ceac.mvvmapp.navigation.HandleNavigationEvents

@Composable
fun HomeEntry(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    val vm: HomeViewModel = hiltViewModel()
    HandleNavigationEvents(navController, snackbarHostState, vm.events)
    val state by vm.state.collectAsState()

    HomeScreen(
        state = state,
        onRetry = vm::load
    )
}