package com.example.volantum.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Detalle de usuario",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("ID: ${user.id}")
                Text("Marca: ${user.firstName}")
                Text("Modelo: ${user.lastName}")
                Text("Año: ${user.score}")
                // …
            }
        }
    }
}