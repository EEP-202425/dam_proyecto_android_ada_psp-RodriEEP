package com.example.volantum.ui.screens.cars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CarsDetailScreen(carId: Int) {
    val viewModel: CarsDetailViewModel = viewModel(
        factory = CarsDetailViewModelFactory(carId)
    )

    val uiState = viewModel.carsDetailUiState

    when (uiState) {
        is CarsDetailUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is CarsDetailUiState.Success -> {
            val car = uiState.car
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Detalle del coche",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("ID: ${car.id}")
                Text("Marca: ${car.brand}")
                Text("Modelo: ${car.model}")
                Text("Año: ${car.yearModel}")
                // …
            }
        }
        is CarsDetailUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡Oops! ${uiState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is CarsUiState.Success -> TODO()
    }
}