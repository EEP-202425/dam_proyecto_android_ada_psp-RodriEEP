package com.example.volantum.ui.screens.sessions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volantum.data.model.DrivingSession
import com.example.volantum.network.DrivingSessionApi
import kotlinx.coroutines.launch

sealed interface SessionsUiState {
    data class Success(val cars: List<DrivingSession>) : SessionsUiState
    data class Error(val message: String) : SessionsUiState
    object Loading : SessionsUiState
}

class SessionsViewModel: ViewModel() {
    var sessionsUiState: SessionsUiState by mutableStateOf(SessionsUiState.Loading)
        private set

    init {
        getDrivingSessions()
    }

    private fun getDrivingSessions() {
        viewModelScope.launch {
            sessionsUiState = try {
                val drivingSession = DrivingSessionApi.retrofitService.getDrivingSessions()
                SessionsUiState.Success(drivingSession)
            } catch (e: Exception) {
                SessionsUiState.Error(e.message.toString())
            }
        }
    }   
}