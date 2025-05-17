package com.example.volantum.ui.screens.cars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.volantum.ui.components.CarCard
import com.example.volantum.ui.navigation.NavigationRouter
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome

@Composable
fun CarsScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: CarsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("refresh")?.let {
            if (it) {
                viewModel.refreshCarsList()
                navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("refresh")
            }
        }
    }

    val uiState = viewModel.carsUiState

    when (uiState) {
        is CarsUiState.Loading -> {
            androidx.compose.material3.CircularProgressIndicator()
        }
        is CarsUiState.Error -> {
            Text((viewModel.carsUiState as CarsUiState.Error).message ?: "Error al cargar los coches")
        }
        is CarsUiState.Success -> {
            val cars = (viewModel.carsUiState as CarsUiState.Success).cars
             if (cars.isEmpty()) {
                Text(
                    text = "No hay coches disponibles.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
                 Box(
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(16.dp),
                     contentAlignment = Alignment.Center
                 ) {
                     Button(
                         onClick = {
                             navController.navigate(NavigationRouter.CarsCreate.route)
                         },
                         colors = ButtonDefaults.buttonColors(
                             containerColor = MaterialTheme.colorScheme.primary
                         ),
                         elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                     ) {
                         Text("Crear coche")
                     }
                 }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .padding(bottom = 60.dp)
                ) {
                    items(cars) { car ->
                        CarCard(car = car, navController = navController)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                
                if (cars.isNotEmpty() && uiState.totalPages > 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .fillMaxWidth()
                            .padding(paddingValues)
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            for (i in 0 until uiState.totalPages) {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .clickable { viewModel.goToPage(i) }
                                        .height(4.dp)
                                        .width(if (i == uiState.currentPage) 24.dp else 12.dp)
                                        .background(
                                            color = if (i == uiState.currentPage)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(2.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}