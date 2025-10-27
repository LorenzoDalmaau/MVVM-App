package com.ceac.mvvmapp.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(val route: String) {
    data object Login : Route("login")
    data object Register : Route("register")
    data object RecoverPassword : Route("recover_password")
    data object Home : Route("home")

    // data object UserDetail : Route("user/{userId}") {
    //     val args = listOf(navArgument("userId") { type = NavType.IntType })
    //     fun build(userId: Int) = "user/$userId"
    // }
}
