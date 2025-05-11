package com.example.volantum.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volantum.data.model.User
import com.example.volantum.network.UserApi
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data class Success(val user: User) : HomeUiState
    data class Error(val message: String) : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel : ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            homeUiState = try {
                val user = UserApi.retrofitService.getUser()
                HomeUiState.Success(user)
            } catch (e: Exception) {
                HomeUiState.Error(e.message.toString())
            }
        }
    }
}
