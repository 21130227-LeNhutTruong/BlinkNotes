package com.example.blinknotes.navigation


sealed class Screens(val route: String) {
    object HomeScreen: Screens("home_screen")
    object DetaillScreen: Screens("detail/{encodedUrl}")
}
