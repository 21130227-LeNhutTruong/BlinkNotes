package com.example.blinknotes


import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.blinknotes.navigation.RootNavigationGraph
import com.example.blinknotes.ui.theme.BlinkNotesTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContent {
            BlinkNotesTheme {
                RootNavigationGraph(navController = rememberNavController())
            }
        }
    }
}

