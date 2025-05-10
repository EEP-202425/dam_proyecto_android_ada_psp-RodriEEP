package com.example.volantum.ui.screens.sessions

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
fun SessionsScreen(
    navController: NavController,
    viewModel: SessionsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    when (viewModel.sessionsUiState) {
        is SessionsUiState.Loading -> {
            androidx.compose.material3.CircularProgressIndicator()
        }
        is SessionsUiState.Error -> {
            Text((viewModel.sessionsUiState as SessionsUiState.Error).message ?: "Error al cargar los coches")
        }
        is SessionsUiState.Success -> {
            val drivingSessions = (viewModel.sessionsUiState as SessionsUiState.Success).cars
            LazyColumn {
                items(drivingSessions) { session ->
                    ListItem(
                        headlineContent = { Text("${session.startTime} ${session.endTime}") },
                        supportingContent = { Text("Distancia: ${session.distance}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("sessions/${session.id}")
                            }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}