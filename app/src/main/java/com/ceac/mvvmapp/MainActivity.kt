package com.ceac.mvvmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ceac.mvvmapp.navigation.AppNav
import com.ceac.mvvmapp.navigation.HandleNavigationEvents
import com.ceac.mvvmapp.ui.screens.auth.login.LoginViewModel
import com.ceac.mvvmapp.ui.screens.login.LoginScreen
import com.ceac.mvvmapp.ui.theme.MVVMAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MVVMAppTheme {
                AppNav(
                    navController = navController,
                    loginScreen = {
                        val vm: LoginViewModel = hiltViewModel()
                        HandleNavigationEvents(navController, vm.events)
                        val state by vm.state.collectAsState()
                        LoginScreen(
                            state = state,
                            onEmailChange = vm::onEmailChange,
                            onPasswordChange = vm::onPasswordChange,
                            onLoginClick = vm::onLoginClick,
                            onRegisterClick = vm::onRegisterClick,
                            onRecoverClick = vm::onRecoverClick
                        )
                    },
                    registerScreen = { Text("Register (stub)") },
                    recoverPasswordScreen = { Text("Recover (stub)") },
                    homeScreen = { Text("Home (stub)") }
                )
            }
        }
    }
}
