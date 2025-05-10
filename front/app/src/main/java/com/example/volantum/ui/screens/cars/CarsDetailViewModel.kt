package com.example.volantum.ui.screens.cars

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.volantum.data.model.Car
import com.example.volantum.network.CarApi
import kotlinx.coroutines.launch

sealed interface CarsDetailUiState {
    data class Success(val car: Car) : CarsDetailUiState
    data class Error(val message: String) : CarsDetailUiState
    object Loading : CarsDetailUiState
}

class CarsDetailViewModel(
    private val carId: Int
): ViewModel() {
    var carsDetailUiState: CarsDetailUiState by mutableStateOf(CarsDetailUiState.Loading)
        private set

    init {
        getCarsDetail(carId)
    }

    private fun getCarsDetail(id: Int) {
        viewModelScope.launch {
            carsDetailUiState = CarsDetailUiState.Loading
            carsDetailUiState = try {
                val car = CarApi.retrofitService.getCarsDetail(id)
                CarsDetailUiState.Success(car)
            } catch (e: Exception) {
                CarsDetailUiState.Error(e.message.toString())
            }
        }
    }
}

class CarsDetailViewModelFactory(
    private val carId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarsDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarsDetailViewModel(carId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}