package com.example.volantum.ui.screens.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.volantum.R
import androidx.compose.ui.res.stringResource
import com.example.volantum.ui.components.SessionCard

@Composable
fun SessionsScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: SessionsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    when (viewModel.sessionsUiState) {
        is SessionsUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is SessionsUiState.Error -> {
            Text(
                (viewModel.sessionsUiState as SessionsUiState.Error).message ?: stringResource(R.string.sessions_error_load)
            )
        }
        is SessionsUiState.Success -> {
            val drivingSessions = (viewModel.sessionsUiState as SessionsUiState.Success).cars
            
            if (drivingSessions.isEmpty()) {
                Text(
                    stringResource(R.string.sessions_no_sessions),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
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
}