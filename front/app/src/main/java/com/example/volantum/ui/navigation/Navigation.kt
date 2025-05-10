package com.example.volantum.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.volantum.ui.components.BottomNavigationBar
import com.example.volantum.ui.screens.cars.CarsDetailScreen
import com.example.volantum.ui.screens.cars.CarsScreen
import com.example.volantum.ui.screens.home.HomeScreen
import com.example.volantum.ui.screens.sessions.SessionsDetailScreen
import com.example.volantum.ui.screens.sessions.SessionsScreen

@Composable
fun App() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = NavigationRouter.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationRouter.Home.route) { HomeScreen() }
            composable(NavigationRouter.Sessions.route) { SessionsScreen(navController) }
            composable(NavigationRouter.Cars.route) { CarsScreen(navController) }
            composable(NavigationRouter.CarsDetail.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                if (id != null) {
                    CarsDetailScreen(id)
                } else {
                }
            }
            composable(NavigationRouter.SessionsDetail.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                if (id != null) {
                    SessionsDetailScreen(id)
                } else {
                }
            }
        }
    }
}