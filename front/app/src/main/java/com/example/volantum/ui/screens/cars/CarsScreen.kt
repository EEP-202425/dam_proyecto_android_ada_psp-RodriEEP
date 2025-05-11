package com.example.volantum.ui.screens.cars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun CarsScreen(
    navController: NavController,
    viewModel: CarsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    when (viewModel.carsUiState) {
        is CarsUiState.Loading -> {
            androidx.compose.material3.CircularProgressIndicator()
        }
        is CarsUiState.Error -> {
            Text((viewModel.carsUiState as CarsUiState.Error).message ?: "Error al cargar los coches")
        }
        is CarsUiState.Success -> {
            val cars = (viewModel.carsUiState as CarsUiState.Success).cars
            LazyColumn {
                items(cars) { car ->
                    ListItem(
                        headlineContent = { Text("${car.brand} ${car.model}") },
                        supportingContent = { Text("Matrícula: ${car.plate}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("cars/${car.id}")
                            }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}