package com.example.blinknotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.blinknotes.ui.Auth.AuthViewModel
import com.example.blinknotes.ui.Auth.LoginScreen
import com.example.blinknotes.ui.Auth.RegisterScreen
import com.example.blinknotes.ui.detaill.DetaillScreen
import com.example.blinknotes.ui.home.HomeScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraphAuth (navController: NavHostController, modifier: Any) {
    LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screens.LoginScreen.route
    ) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController = navController, authViewModel = AuthViewModel())
        }
        composable(Screens.RegisterScreen.route) {
            RegisterScreen(navController = navController, authViewModel = AuthViewModel() )
        }
//        composable(route = Screens.HomeScreen.route) {
//            HomeScreen(navController = navController)
//        }
//        composable(route = Screens.DetaillScreen.route) { backStackEntry ->
//            val encodedUrl = backStackEntry.arguments?.getString("encodedUrl")
//            val decodedUrl =
//                encodedUrl?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
//            decodedUrl?.let {
//                DetaillScreen(navController = navController, imageUrl = it)
//            }
//        }
    }
}