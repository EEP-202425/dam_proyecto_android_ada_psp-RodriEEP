package com.example.volantum.ui.navigation

sealed class NavigationRouter(val route: String, val title: String) {
    object Home : NavigationRouter("home", "Volantum")
    object Sessions : NavigationRouter("sessions", "Tus sesiones")
    object Cars : NavigationRouter("cars", "Tus coches")
    object CarsDetail: NavigationRouter("cars/{id}", "Detalle de coche")
    object SessionsDetail: NavigationRouter("sessions/{id}", "Detalle de sesión")
    object CarsEdit : NavigationRouter("cars/{id}/edit", "Editar coche")
}