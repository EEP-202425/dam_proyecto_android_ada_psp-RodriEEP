package com.example.volantum.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.volantum.ui.components.AppTopBar
import com.example.volantum.ui.components.BottomNavigationBar
import com.example.volantum.ui.screens.cars.CarsDetailScreen
import com.example.volantum.ui.screens.cars.CarsEditScreen
import com.example.volantum.ui.screens.cars.CarsScreen
import com.example.volantum.ui.screens.home.HomeScreen
import com.example.volantum.ui.screens.sessions.SessionsDetailScreen
import com.example.volantum.ui.screens.sessions.SessionsScreen

@Composable
fun App() {
    val navController = rememberNavController()
    var currentTitle by remember { mutableStateOf(NavigationRouter.Home.title) }

    // Track current route for title updates
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            currentTitle = when {
                route == NavigationRouter.Home.route -> NavigationRouter.Home.title
                route == NavigationRouter.Sessions.route -> NavigationRouter.Sessions.title
                route == NavigationRouter.Cars.route -> NavigationRouter.Cars.title
                route?.startsWith("cars/") == true && route.endsWith("/edit") -> NavigationRouter.CarsEdit.title
                route?.startsWith("cars/") == true -> NavigationRouter.CarsDetail.title
                route?.startsWith("sessions/") == true -> NavigationRouter.SessionsDetail.title
                else -> "Volantum"
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                currentScreen = currentTitle,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )},
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = NavigationRouter.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(NavigationRouter.Home.route) { HomeScreen(navController, paddingValues = paddingValues) }
            composable(NavigationRouter.Sessions.route) { SessionsScreen(navController, paddingValues = paddingValues) }
            composable(NavigationRouter.Cars.route) { CarsScreen(navController, paddingValues = paddingValues) }
            composable(NavigationRouter.CarsDetail.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                if (id != null) {
                    CarsDetailScreen(id, paddingValues = paddingValues, navController)
                }
            }
            composable(NavigationRouter.SessionsDetail.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                if (id != null) {
                    SessionsDetailScreen(id, paddingValues = paddingValues)
                } else {
                }
            }
            composable(NavigationRouter.CarsEdit.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                if (id != null) {
                    CarsEditScreen(id, paddingValues = paddingValues, navController)
                }
            }
        }
    }
}