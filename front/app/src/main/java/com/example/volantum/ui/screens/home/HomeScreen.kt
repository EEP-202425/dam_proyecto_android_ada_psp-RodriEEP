package com.example.volantum.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.volantum.ui.components.SessionCard
import com.example.volantum.ui.components.CarCard
import com.example.volantum.ui.navigation.NavigationRouter

@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val uiState = viewModel.homeUiState

    when (uiState) {
        is HomeUiState.Loading -> {
            androidx.compose.material3.CircularProgressIndicator()
        }
        is HomeUiState.Error -> {
            Text(uiState.message ?: "Error al cargar perfil")
        }
        is HomeUiState.Success -> {
           val user = uiState.user
            val lastSession = user.drivingSessions?.firstOrNull()
            val sweepAngle = (user.score.toFloat() / 5f) * 360f
            val arcColor = when {
                user.score >= 4 -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.tertiary
            }
            val arcBackgroundColor = arcColor.copy(alpha = 0.2f)
            val motivationalMessage = when {
                user.score >= 4 -> "¡Excelente! Manejas con mucha responsabilidad."
                else -> "¡Aún puedes mejorar! Sigue así."
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                item {
                    Text(
                        text = "Hola, ${user.firstName}!",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        "Tu última sesión",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (lastSession != null) {
                        SessionCard(session = lastSession, navController = navController)
                    } else {
                        Text("No tienes sesiones anteriores.")
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        "Tu puntuación",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(80.dp)
                            ) {
                                Canvas(modifier = Modifier.size(80.dp)) {
                                    drawArc(
                                        color = arcBackgroundColor,
                                        startAngle = 0f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = Stroke(width = 12f, cap = StrokeCap.Round)
                                    )
                                    drawArc(
                                        color = arcColor,
                                        startAngle = -90f,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(width = 12f, cap = StrokeCap.Round)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${user.score}/5",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = arcColor
                                    )
                                    Text("⭐", fontSize = 18.sp)
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    motivationalMessage,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Sesiones esta semana: ${user.drivingSessions?.size ?: 0}")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        "Tus coches",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (user.cars?.isEmpty() == true) {
                    item {
                        Text(
                            "No tienes coches disponibles.",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                } else {
                    items(user.cars ?: emptyList()) { car ->
                        CarCard(car = car, navController = navController)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                item {
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
                }
            }
        }
    }
}