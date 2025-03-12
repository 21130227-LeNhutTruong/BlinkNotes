package com.example.blinknotes.navigation


sealed class Screens(val route: String) {
    object HomeScreen: Screens("home_screen")
    object DetaillScreen: Screens("detail/{encodedUrl}")
    object AddPhotoScreen: Screens("add_photo")
    object LoginScreen: Screens("login_screen")
    object RegisterScreen: Screens("register_screen")
    object SearchScreen : Screens("search_screen")
    object ProfileScreen: Screens("profile_screen")
    object NotifyScreen: Screens("notify_screen")
    object PlashScreen: Screens("plash_screen")
    object ShowBottomBar: Screens("main_screen")
    object SettingScreenProfile: Screens("setting_screen_profile")
    object PhoneAuthScreen: Screens("phone_auth_screen/{email}/{userName}/{password}/{confirmPassword}"){
        fun createRoute(email: String, userName: String,password: String,confirmPassword:String ) =
            "phone_auth_screen/$email/$userName/$password/$confirmPassword"
    }

}
