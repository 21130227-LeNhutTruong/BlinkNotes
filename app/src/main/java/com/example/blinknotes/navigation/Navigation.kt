package com.example.blinknotes.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material3.IconButton


data class NavigationItem(
    val icon: ImageVector,
    val iconOutline : ImageVector,
    val route: String
)

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<NavigationItem>) {

    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    BottomNavigation (
        backgroundColor = Color.White
    ) {
        items.forEach { item ->
            if (item.icon == Icons.Default.Add) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(onClick = {
                        navController.navigate(Screens.AddPhotoScreen.route)
                    }) {
                        Icon(imageVector = item.icon, contentDescription = null)
                    }
                }
            } else {
                IconButton(
                    onClick = {
                        selected.value = item.icon
                        navController.navigate(item.route) {
                            popUpTo(Screens.HomeScreen.route) { inclusive = false }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == item.icon) Color.Black else Color.LightGray
                    )
                }
            }
        }
    }
}