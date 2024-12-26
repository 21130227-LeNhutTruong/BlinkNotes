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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blinknotes.navigation.NavGraphAuth
import com.example.blinknotes.navigation.Navigation
import com.example.blinknotes.navigation.Screens
import com.example.blinknotes.ui.Auth.AuthViewModel
import com.example.blinknotes.ui.Auth.LoginScreen
import com.example.blinknotes.ui.Auth.RegisterScreen
import com.example.blinknotes.ui.home.HomeScreen
import com.example.blinknotes.ui.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          //MyApp()
            Navigation()
        }
    }
}

@Composable
fun MyApp() {
    var userSignedIn by remember { mutableStateOf(false) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser != null) {
        userSignedIn = true
    }
    var showSplash by remember { mutableStateOf(true) }

    val navController: NavHostController = rememberNavController()

    LocalContext.current
    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    } else if (userSignedIn) {
        NavGraphAuth(navController = navController, modifier = Any())
    }else{
        Navigation()
    }
}

