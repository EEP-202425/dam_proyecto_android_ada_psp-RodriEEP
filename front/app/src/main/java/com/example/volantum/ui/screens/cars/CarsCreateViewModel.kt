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

sealed interface CarsCreateUiState {
    object Initial : CarsCreateUiState
    object Loading  : CarsCreateUiState
    object Error : CarsCreateUiState
    data class Success (val car: Car): CarsCreateUiState
}

class CarsCreateViewModel : ViewModel() {
    var carsCreateUiState: CarsCreateUiState by mutableStateOf(CarsCreateUiState.Initial)
        private set

    fun createCar(car: Car, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            carsCreateUiState = CarsCreateUiState.Loading
            try {
                val createdCar = CarApi.retrofitService.createCar(car)
                carsCreateUiState = CarsCreateUiState.Success(createdCar)
                onSuccess(true)
            } catch (e: Exception) {
                carsCreateUiState = CarsCreateUiState.Error
                onSuccess(false)
            }
        }
    }
}

class CarsCreateViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarsCreateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarsCreateViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

