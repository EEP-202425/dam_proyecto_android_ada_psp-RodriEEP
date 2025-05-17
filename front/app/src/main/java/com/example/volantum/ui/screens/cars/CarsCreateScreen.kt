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
import com.example.volantum.ui.navigation.NavigationRouter
import com.example.volantum.R
import androidx.compose.ui.res.stringResource

@Composable
fun CarsCreateScreen(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val viewModel: CarsCreateViewModel = viewModel(
        factory = CarsCreateViewModelFactory()
    )

    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var yearModel by remember { mutableStateOf("") }

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
            label = { Text(stringResource(R.string.car_brand)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text(stringResource(R.string.car_model)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = plate,
            onValueChange = { plate = it },
            label = { Text(stringResource(R.string.car_plate)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = yearModel,
            onValueChange = { yearModel = it },
            label = { Text(stringResource(R.string.car_year)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        when (viewModel.carsCreateUiState) {
            CarsCreateUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            CarsCreateUiState.Error -> {
                Text(
                    stringResource(R.string.cars_create_error_create),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {
                Button(
                    onClick = {
                        val newCar = Car(
                            id = 0,
                            brand = brand,
                            model = model,
                            plate = plate,
                            yearModel = yearModel.toIntOrNull() ?: 0,
                            mileage = 0.0f,
                            image = ""
                        )
                        viewModel.createCar(newCar) { success ->
                            if (success) {
                                navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                                navController.navigate(NavigationRouter.Cars.route)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(stringResource(R.string.create_car))
                }
            }
        }
    }
} 