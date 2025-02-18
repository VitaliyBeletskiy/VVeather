package vibe.weather

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vibe.weather.ui.screens.home.HomeScreen
import vibe.weather.ui.screens.locations.LocationsScreen
import vibe.weather.ui.screens.locations.LocationsViewModel
import vibe.weather.utils.logD

enum class AppScreens {
    HomeScreen,
    LocationsScreen,
}

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.name) {
        composable(AppScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }

        composable(AppScreens.LocationsScreen.name) {
            LocationsScreen(
                viewModel = hiltViewModel<LocationsViewModel>(),
                navigateBack = { navController.popBackStack() },
                onLocationChosen = { location ->
                    logD("Location chosen: $location")
                    navController.popBackStack()
                },
            )
        }
    }
}
