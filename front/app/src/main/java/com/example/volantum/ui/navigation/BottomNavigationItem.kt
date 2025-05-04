package com.example.volantum.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavigationItem("home", Icons.Default.Home, "Inicio")
    object Sessions : BottomNavigationItem("sessions", Icons.AutoMirrored.Filled.List, "Sesiones")
    object Cars : BottomNavigationItem("cars", Icons.Default.Build, "Coches")
}