package com.example.blinknotes.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blinknotes.ui.Auth.AuthViewModel
import com.example.blinknotes.ui.Auth.LoginScreen
import com.example.blinknotes.ui.Auth.RegisterScreen
import com.example.blinknotes.ui.splash.SplashScreen
import com.example.myapp_use_jetpakcompose.Navigation.NavGraph
import com.google.firebase.auth.FirebaseAuth


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val screen: Screens
)
@Composable
fun Navigation(

) {
    LocalContext.current
    val navController: NavHostController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberSaveable {
        mutableStateOf(0)
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    backStackEntry?.destination?.route ?: Screens.HomeScreen

    listOf(
        Screens.HomeScreen,
        Screens.DetaillScreen,
        Screens.AddPhotoScreen

    )
    val items = mutableListOf(
        NavigationItem(
            title = "Home",
            selectedIcon = Icons.Outlined.Home,
            unselectedIcon = Icons.Filled.Home,
            screen = Screens.HomeScreen
        ),
        NavigationItem(
            title ="addPhoto",
            selectedIcon = Icons.Outlined.Add,
            unselectedIcon = Icons.Filled.Add,
            screen = Screens.AddPhotoScreen
        ),
        NavigationItem(
            title ="Screen2",
            selectedIcon = Icons.Outlined.List,
            unselectedIcon = Icons.Filled.List,
            screen = Screens.DetaillScreen
        )


    )

    var showSplash by remember { mutableStateOf(true) }
    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    } else  {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController = navController, items = items)
            }
        ) { innerPadding ->
            NavGraph(navController = navController, modifier = innerPadding)
        }
    }
}
@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<NavigationItem>) {
    NavigationBar(
        modifier = Modifier.height(70.dp)
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            val isSelected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(bottom = 4.dp),
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = if (isSelected) Color.Blue else Color.Black
                    )
                },
                label = {

                },
                alwaysShowLabel = false
            )
        }
    }
}