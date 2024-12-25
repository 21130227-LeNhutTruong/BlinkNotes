package com.example.blinknotes


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blinknotes.data.DataImage
import com.example.blinknotes.navigation.Navigation
import com.example.blinknotes.ui.home.HomeScreen
import com.example.blinknotes.ui.detaill.DetaillScreen
import com.example.blinknotes.ui.splash.SplashScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
            Navigation()
           // NavGraph()
        }
    }
}

@Composable
fun MyApp() {

    var showSplash by remember { mutableStateOf(true) }

    val navController = rememberNavController()

    LocalContext.current
    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    } else {
        HomeScreen(navController = navController)
        Navigation()
    }

}


//
//@Composable
//fun NavGraph() {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = "home") {
//        composable("home") {
//            HomeScreen(navController = navController)
//        }
//        composable("detail/{encodedUrl}") { backStackEntry ->
//            val encodedUrl = backStackEntry.arguments?.getString("encodedUrl")
//            val decodedUrl = encodedUrl?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
//
//            decodedUrl?.let {
//                // Sử dụng URL decoded để tải ảnh hoặc thực hiện các thao tác khác
//                DetaillScreen(navController = navController, imageUrl = it)
//            }
//        }
//    }
//}
//
