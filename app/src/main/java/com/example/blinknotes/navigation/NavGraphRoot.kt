package com.example.blinknotes.navigation

import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import authNavGraph
import navGraph

@Composable
fun RootNavigationGraph(navController: NavHostController) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val homeRoutes = listOf(
        Screens.HomeScreen.route,
        Screens.AddPhotoScreen.route,
        Screens.ProfileScreen.route,
        Screens.SearchScreen.route,
        Screens.NotifyScreen.route,
    )

    val bottomNavigationItems = listOf(
        NavigationItem(Icons.Filled.Home,Icons.Outlined.Home, Screens.HomeScreen.route),
        NavigationItem(Icons.Filled.Search,Icons.Outlined.Search,Screens.SearchScreen.route),
        NavigationItem(Icons.Filled.Add,Icons.Outlined.Add,Screens.AddPhotoScreen.route),
        NavigationItem(Icons.Filled.Notifications,Icons.Outlined.Notifications, Screens.NotifyScreen.route),
        NavigationItem(Icons.Filled.Person,Icons.Outlined.Person, Screens.ProfileScreen.route),
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in homeRoutes) {
                BottomNavigationBar(navController = navController, items = bottomNavigationItems)
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            route = Graph.ROOT,
            startDestination = Graph.AUTHENTICATION,
        ) {
            authNavGraph(navController)
            navGraph(navController, modifier = paddingValues)
        }
    }
}
object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}