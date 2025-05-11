package com.example.volantum.ui.screens.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.volantum.ui.components.SessionCard

@Composable
fun SessionsScreen(
    navController: NavController,
    paddingValues: PaddingValues,
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                items(drivingSessions) { session ->
                    SessionCard(session = session, navController = navController)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}