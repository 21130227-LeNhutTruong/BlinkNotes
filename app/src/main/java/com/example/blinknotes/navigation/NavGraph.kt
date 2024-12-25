package com.example.myapp_use_jetpakcompose.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.blinknotes.navigation.Screens
import com.example.blinknotes.ui.detaill.DetaillScreen
import com.example.blinknotes.ui.home.HomeScreen
import com.example.blinknotes.ui.home.HomeScreenViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun NavGraph (navController: NavHostController, modifier: Any) {
    LocalContext.current
    val viewModel = viewModel<HomeScreenViewModel>()

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screens.DetaillScreen.route) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("encodedUrl")
            val decodedUrl =
                encodedUrl?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }

            decodedUrl?.let {
                // Sử dụng URL decoded để tải ảnh hoặc thực hiện các thao tác khác
                DetaillScreen(navController = navController, imageUrl = it)
            }

        }
    }
}