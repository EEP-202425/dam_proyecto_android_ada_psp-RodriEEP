package com.example.volantum.ui.screens.cars

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.volantum.data.model.Car

@Composable
fun CarsEditScreen(
    carId: Int,
    paddingValues: PaddingValues,
    navController: NavController
) {
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    val initialCar = savedStateHandle?.get<Car>("car")
    
    val viewModel: CarsEditViewModel = viewModel(
        factory = CarsEditViewModelFactory(carId, initialCar)
    )

    val uiState = viewModel.carsEditUiState

    when (uiState) {
        is CarsEditUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is CarsEditUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error al cargar los datos del coche")
            }
        }
        is CarsEditUiState.Success -> {
            var brand by remember { mutableStateOf(uiState.car.brand) }
            var model by remember { mutableStateOf(uiState.car.model) }
            var plate by remember { mutableStateOf(uiState.car.plate) }
            var yearModel by remember { mutableStateOf(uiState.car.yearModel.toString()) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marca") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Modelo") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    label = { Text("Matrícula") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = yearModel,
                    onValueChange = { yearModel = it },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val updatedCar = Car(
                            id = carId,
                            brand = brand,
                            model = model,
                            plate = plate,
                            yearModel = yearModel.toIntOrNull() ?: 0,
                            mileage = uiState.car.mileage,
                            image = uiState.car.image
                        )
                        viewModel.updateCar(updatedCar) { success ->
                            if (success) {
                                navController.previousBackStackEntry?.savedStateHandle?.set("carUpdated", true)
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Guardar cambios")
                }
            }
        }
    }
} 