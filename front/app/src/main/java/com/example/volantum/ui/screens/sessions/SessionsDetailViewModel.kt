package com.example.volantum.ui.screens.sessions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.volantum.data.model.DrivingSession
import com.example.volantum.network.DrivingSessionApi
import kotlinx.coroutines.launch

sealed interface SessionsDetailUiState {
    data class Success(val session: DrivingSession) : SessionsDetailUiState
    data class Error(val message: String) : SessionsDetailUiState
    object Loading : SessionsDetailUiState
}

class SessionsDetailViewModel(
    private val sessionId: Int
): ViewModel() {
    var sessionsDetailUiState: SessionsDetailUiState by mutableStateOf(SessionsDetailUiState.Loading)
        private set

    init {
        getSessionsDetail(sessionId)
    }

    private fun getSessionsDetail(id: Int) {
        viewModelScope.launch {
            sessionsDetailUiState = SessionsDetailUiState.Loading
            sessionsDetailUiState = try {
                val session = DrivingSessionApi.retrofitService.getDrivingSessionDetail(id)
                SessionsDetailUiState.Success(session)
            } catch (e: Exception) {
                SessionsDetailUiState.Error(e.message.toString())
            }
        }
    }
}

class SessionsDetailViewModelFactory(
    private val sessionId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionsDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SessionsDetailViewModel(sessionId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}