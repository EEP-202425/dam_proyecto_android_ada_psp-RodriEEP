package com.example.volantum.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home

sealed class NavigationRouter(val route: String) {
    object Home : NavigationRouter("home")
    object Sessions : NavigationRouter("sessions")
    object Cars : NavigationRouter("cars")
    object CarsDetail: NavigationRouter("cars/{id}")
}