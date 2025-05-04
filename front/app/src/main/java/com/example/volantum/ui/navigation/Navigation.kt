package com.example.volantum.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.volantum.ui.components.BottomNavigationBar
import com.example.volantum.ui.screens.cars.CarsScreen
import com.example.volantum.ui.screens.home.HomeScreen
import com.example.volantum.ui.screens.sessions.SessionsScreen

@Composable
fun App() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = BottomNavigationItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavigationItem.Home.route) { HomeScreen() }
            composable(BottomNavigationItem.Sessions.route) { SessionsScreen() }
            composable(BottomNavigationItem.Cars.route) { CarsScreen() }
        }
    }
}