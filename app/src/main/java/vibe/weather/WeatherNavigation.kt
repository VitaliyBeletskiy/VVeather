package vibe.weather

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import vibe.weather.ui.screens.home.HomeScreen
import vibe.weather.ui.screens.search.SearchScreen

enum class AppScreens {
    HomeScreen,
    SearchScreen,
}

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.name) {
        composable(AppScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }

        composable(AppScreens.SearchScreen.name) {
            SearchScreen(navController = navController)
        }
    }
}
