package com.example.volantum.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome

sealed class BottomNavigationItem(val route: String, val icon: FontAwesome.Icon, val label: String) {
    object Home : BottomNavigationItem("home", FontAwesome.Icon.faw_home, "Inicio")
    object Sessions : BottomNavigationItem("sessions", FontAwesome.Icon.faw_clock, "Sesiones")
    object Cars : BottomNavigationItem("cars", FontAwesome.Icon.faw_car, "Coches")
}