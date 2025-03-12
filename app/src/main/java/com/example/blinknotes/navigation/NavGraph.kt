import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.blinknotes.navigation.Graph
import com.example.blinknotes.navigation.Screens
import com.example.blinknotes.ui.addPhoto.AddPhotoScreen
import com.example.blinknotes.ui.detaill.DetaillScreen
import com.example.blinknotes.ui.home.HomeScreen
import com.example.blinknotes.ui.home.HomeScreenViewModel
import com.example.blinknotes.ui.notify.NotifyScreen
import com.example.blinknotes.ui.profile.ProfileScreen
import com.example.blinknotes.ui.profile.settingProfile.SettingScreenProfile
import com.example.blinknotes.ui.search.SearchScreen
import com.example.blinknotes.ui.search.SearchScreenViewModelFactory
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


fun NavGraphBuilder.navGraph(navController: NavHostController, modifier: Any,
                             viewModel: HomeScreenViewModel
) {

    navigation(
        route = Graph.HOME,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(route = Screens.HomeScreen.route) {
            HomeScreen(navController = navController, viewmodel = viewModel )
        }
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(route = Screens.SearchScreen.route) {
            SearchScreen(navController = navController, viewModel = SearchScreenViewModelFactory())
        }
        composable(route = Screens.NotifyScreen.route) {
            NotifyScreen(navController = navController)
        }
        detailsNavGraph(navController)
        // addPhotoNavGraph(navController)
        composable(
            route = "${Screens.AddPhotoScreen.route}?imageUris={imageUris}",
            arguments = listOf(navArgument("imageUris") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageUris = backStackEntry.arguments?.getString("imageUris")
            AddPhotoScreen(navController = navController, imageUris = imageUris)
        }
        composable(route = Screens.SettingScreenProfile.route){
            SettingScreenProfile(navController = navController)
        }


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
