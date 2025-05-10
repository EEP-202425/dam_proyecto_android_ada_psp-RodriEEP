package com.example.volantum.ui.screens.cars

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volantum.data.model.Car
import com.example.volantum.network.CarApi
import kotlinx.coroutines.launch

sealed interface CarsUiState {
    data class Success(val cars: List<Car>) : CarsUiState, CarsDetailUiState
    data class Error(val message: String) : CarsUiState
    object Loading : CarsUiState
}

class CarsViewModel: ViewModel() {
    var carsUiState: CarsUiState by mutableStateOf(CarsUiState.Loading)
        private set

    init {
        getCars()
    }

    private fun getCars() {
        viewModelScope.launch {
            carsUiState = try {
                val cars = CarApi.retrofitService.getCars()
                CarsUiState.Success(cars)
            } catch (e: Exception) {
                CarsUiState.Error(e.message.toString())
            }
        }
    }   
}