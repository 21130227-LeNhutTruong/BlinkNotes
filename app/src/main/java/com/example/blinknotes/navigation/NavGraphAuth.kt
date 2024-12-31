import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.blinknotes.navigation.Graph
import com.example.blinknotes.navigation.Screens
import com.example.blinknotes.ui.Auth.AuthViewModel
import com.example.blinknotes.ui.Auth.LoginScreen
import com.example.blinknotes.ui.Auth.RegisterScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Screens.LoginScreen.route
    ) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(
                authViewModel = AuthViewModel(),
                navController = navController,
            )
        }

        composable(Screens.RegisterScreen.route) {
            RegisterScreen(
                navController = navController,
                authViewModel = AuthViewModel()
            )
        }

    }
}