package com.example.volantum.ui.screens.sessions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
fun SessionsDetailScreen(
    sessionId: Int,
    paddingValues: PaddingValues
) {
    val viewModel: SessionsDetailViewModel = viewModel(
        factory = SessionsDetailViewModelFactory(sessionId)
    )

    val uiState = viewModel.sessionsDetailUiState

    when (uiState) {
        is SessionsDetailUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is SessionsDetailUiState.Success -> {
            val session = uiState.session
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Detalle de sesión",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("ID: ${session.id}")
                Text("Distancia: ${session.distance}")
                Text("Inicio: ${session.startTime}")
                Text("Fin: ${session.endTime}")
                // …
            }
        }
        is SessionsDetailUiState.Error -> {
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
    }
}