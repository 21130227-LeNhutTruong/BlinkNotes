package com.example.blinknotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.blinknotes.ui.Auth.AuthViewModel
import com.example.blinknotes.ui.Auth.LoginScreen
import com.example.blinknotes.ui.Auth.RegisterScreen
import com.example.myapp_use_jetpakcompose.Navigation.NavGraph

@Composable
fun NavGraphAuth (navController: NavHostController, modifier: Any) {
    LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route
    ) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(authViewModel = AuthViewModel(),
                navController = navController,
            )
        }
        composable(Screens.RegisterScreen.route) {
            RegisterScreen(navController = navController, authViewModel = AuthViewModel())
        }

    }
}