package com.example.blinknotes.navigation


sealed class Screens(val route: String) {
    object HomeScreen: Screens("home_screen")
    object DetaillScreen: Screens("detail/{encodedUrl}")
    object AddPhotoScreen: Screens("add_photo")
    object LoginScreen: Screens("login_screen")
    object RegisterScreen: Screens("register_screen")
}
