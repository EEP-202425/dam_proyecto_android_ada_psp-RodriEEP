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

sealed interface CarsEditUiState {
    data class Success(val car: Car) : CarsEditUiState
    object Error : CarsEditUiState
    object Loading : CarsEditUiState
}

class CarsEditViewModel(
    private val carId: Int,
    initialCar: Car? = null
) : ViewModel() {
    var carsEditUiState: CarsEditUiState by mutableStateOf(
        if (initialCar != null) CarsEditUiState.Success(initialCar)
        else CarsEditUiState.Loading
    )
        private set

    init {
        if (initialCar == null) {
            getCarsDetail()
        }
    }

    private fun getCarsDetail() {
        viewModelScope.launch {
            carsEditUiState = try {
                CarsEditUiState.Success(
                    CarApi.retrofitService.getCarsDetail(carId)
                )
            } catch (e: Exception) {
                CarsEditUiState.Error
            }
        }
    }

    fun updateCar(car: Car, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                CarApi.retrofitService.updateCar(carId, car)
                onSuccess(true)
            } catch (e: Exception) {
                onSuccess(false)
            }
        }
    }
}

class CarsEditViewModelFactory(
    private val carId: Int,
    private val initialCar: Car? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarsEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarsEditViewModel(carId, initialCar) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 