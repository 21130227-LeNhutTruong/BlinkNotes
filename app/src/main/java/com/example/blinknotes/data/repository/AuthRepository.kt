package com.example.blinknotes.data.repository

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 9001
    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login Successful")
                } else {
                    val message = when {
                        task.exception is FirebaseAuthInvalidCredentialsException -> "Incorrect password"
                        task.exception is FirebaseAuthInvalidUserException -> "Email does not exist"
                        else -> task.exception?.localizedMessage ?: "Login failed"
                    }
                    callback(false, message)
                }
            }
    }
    fun registerUser(email: String, password: String, confirmPassword: String, callback: (Boolean, String) -> Unit) {
        if (password != confirmPassword) {
            callback(false, "Passwords do not match")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Registration Successful")
                } else {
                    val message = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> "Email already exists"
                        else -> task.exception?.localizedMessage ?: "Registration failed"
                    }
                    callback(false, message)
                }
            }
    }
//    val googleSignInClient = GoogleSignIn.getClient(
//        context,
//        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(context.getString(R.string.default_web_client_id)) // Web Client ID từ Firebase
//            .requestEmail()
//            .build()
//    )
//    fun signInWithGoogle(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
//        val signInIntent = googleSignInClient.signInIntent
//        launcher.launch(signInIntent)
//    }
    fun googleSignIn(activity: Activity, callback: (Boolean, String) -> Unit) {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(context.getString(R.string.default_web_client_id)) // Lấy ID từ Firebase
//            .requestEmail()
//            .build()
        val googleSignInClient = GoogleSignIn.getClient(activity, GoogleSignInOptions.DEFAULT_SIGN_IN)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}
