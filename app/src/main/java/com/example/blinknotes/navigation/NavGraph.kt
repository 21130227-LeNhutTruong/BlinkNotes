import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.blinknotes.navigation.Graph
import com.example.blinknotes.navigation.Screens
import com.example.blinknotes.ui.addPhoto.AddPhotoScreen
import com.example.blinknotes.ui.detaill.DetaillScreen
import com.example.blinknotes.ui.home.HomeScreen
import com.example.blinknotes.ui.notify.NotifyScreen
import com.example.blinknotes.ui.profile.ProfileScreen
import com.example.blinknotes.ui.search.SearchScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


fun NavGraphBuilder.navGraph( navController: NavHostController, modifier: Any){
    navigation(
        route = Graph.HOME,
        startDestination = Screens.HomeScreen.route) {
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screens.AddPhotoScreen.route) {
            AddPhotoScreen(navController = navController)
        }
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(route = Screens.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(route = Screens.NotifyScreen.route) {
            NotifyScreen(navController = navController)
        }
        detailsNavGraph(navController)
    }
}
fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = Screens.DetaillScreen.route
    ) {
            composable(route = Screens.DetaillScreen.route) { backStackEntry ->
                val encodedUrl = backStackEntry.arguments?.getString("encodedUrl")
                val decodedUrl =
                    encodedUrl?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
                decodedUrl?.let {
                    DetaillScreen(navController = navController, imageUrl = it)
                }
            }
    }
}