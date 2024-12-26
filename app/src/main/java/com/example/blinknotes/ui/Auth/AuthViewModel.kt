package com.example.blinknotes.ui.Auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.blinknotes.data.repository.AuthRepository

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        authRepository.loginUser(email, password, callback)
    }

    fun registerUser(email: String, password: String, confirmPassword: String, callback: (Boolean, String) -> Unit) {
        authRepository.registerUser(email, password, confirmPassword, callback)
    }

    fun googleSignIn(activity: Activity, callback: (Boolean, String) -> Unit) {
        authRepository.googleSignIn(activity, callback)
    }
}
