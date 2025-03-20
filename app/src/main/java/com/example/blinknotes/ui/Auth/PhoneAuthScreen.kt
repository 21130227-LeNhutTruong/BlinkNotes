package com.example.blinknotes.ui.Auth

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.blinknotes.navigation.Screens

@Composable
fun PhoneAuthScreen(authViewModel: AuthViewModel, navController: NavController, navBackStackEntry: NavBackStackEntry) {
    val email = navBackStackEntry.arguments?.getString("email") ?: ""
    val userName = navBackStackEntry.arguments?.getString("userName") ?: ""
    val password = navBackStackEntry.arguments?.getString("password") ?: ""
    val confirmPassword = navBackStackEntry.arguments?.getString("confirmPassword") ?: ""

    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Phone Authentication", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        if (!isOtpSent) {
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Enter phone number (+84...)") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    authViewModel.sendOtp(phoneNumber, activity = activity ) { success, message ->
                        if (success) isOtpSent = true
                        else errorMessage = message
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Send OTP")
            }
        } else {
            TextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    authViewModel.verifyOtp(otp) { success, message ->
                        if (success) {
                            authViewModel.registerUser(
                                email = email,
                                password = password,
                                confirmPassword = confirmPassword,
                                username = userName ,
                                profileImage ="" ) { success, message ->
                                if (success) {
                                    navController.navigate(Screens.LoginScreen.route)
                                } else {
                                    errorMessage = message
                                }
                            }
                            navController.navigate(Screens.LoginScreen.route)

                        } else {
                            errorMessage = message
                        }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Verify OTP")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
