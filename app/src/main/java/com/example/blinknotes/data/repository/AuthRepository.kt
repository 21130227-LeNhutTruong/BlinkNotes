package com.example.blinknotes.data.repository

import android.app.Activity
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
                    val message = when {
                        task.exception is FirebaseAuthUserCollisionException -> "Email already exists"
                        else -> task.exception?.localizedMessage ?: "Registration failed"
                    }
                    callback(false, message)
                }
            }
    }
    fun googleSignIn(activity: Activity, callback: (Boolean, String) -> Unit) {
        val googleSignInClient = GoogleSignIn.getClient(activity, GoogleSignInOptions.DEFAULT_SIGN_IN)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}
