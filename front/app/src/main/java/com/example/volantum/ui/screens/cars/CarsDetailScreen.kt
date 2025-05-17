package com.example.volantum.ui.screens.cars

import com.example.volantum.R
import androidx.compose.ui.graphics.ColorFilter
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
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.volantum.ui.navigation.NavigationRouter
import kotlinx.coroutines.launch

@Composable
fun CarsDetailScreen(
    carId: Int,
    paddingValues: PaddingValues,
    navController: NavController
) {
    val viewModel: CarsDetailViewModel = viewModel(
        factory = CarsDetailViewModelFactory(carId)
    )

    // Observar si hubo una actualización
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("carUpdated")?.let {
            if (it) {
                viewModel.refreshCarDetails()
                navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("carUpdated")
            }
        }
    }

    val uiState = viewModel.carsDetailUiState
    val scope = rememberCoroutineScope()

    when (uiState) {
        is CarsDetailUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is CarsDetailUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.cars_detail_error_load))
            }
        }
        is CarsDetailUiState.Success -> {
            val car = uiState.car
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Image(
                    painterResource(id = R.drawable.car_placeholder),
                    contentDescription = stringResource(
                        R.string.cars_detail_image_content_desc,
                        car.brand,
                        car.model
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "${car.brand} ${car.model}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Text(
                        text = car.plate,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.car_year),
                            value = "${car.yearModel}",
                            icon = FontAwesome.Icon.faw_calendar
                        )
                        InfoCard(
                            modifier = Modifier.weight(1f),
                            title = stringResource(R.string.car_kilometers),
                            value = "${car.mileage}",
                            icon = FontAwesome.Icon.faw_car
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { 
                            navController.currentBackStackEntry?.savedStateHandle?.set("car", uiState.car)
                            navController.navigate("cars/${car.id}/edit") 
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Image(
                            asset = FontAwesome.Icon.faw_edit,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.cars_detail_edit_car))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.deleteCar(carId) { success ->
                                if (success) {
                                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh", true)
                                    navController.navigate(NavigationRouter.Cars.route)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Image(
                            asset = FontAwesome.Icon.faw_trash,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.cars_detail_delete_car))
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: FontAwesome.Icon
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                asset = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}