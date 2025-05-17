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
    data class Success(
        val cars: List<Car>,
        val currentPage: Int,
        val totalPages: Int
    ) : CarsUiState
    data class Error(val message: String) : CarsUiState
    object Loading : CarsUiState
}

class CarsViewModel: ViewModel() {
    var carsUiState: CarsUiState by mutableStateOf(CarsUiState.Loading)
        private set

    private var currentPage = 0
    private val pageSize = 3

    init {
        getCars()
    }

    private fun getCars(page: Int = 0) {
        viewModelScope.launch {
            carsUiState = CarsUiState.Loading
            try {
                val response = CarApi.retrofitService.getCars(page, pageSize)
                carsUiState = CarsUiState.Success(
                    cars = response.content,
                    currentPage = response.number,
                    totalPages = response.totalPages
                )
                currentPage = response.number
            } catch (e: Exception) {
                carsUiState = CarsUiState.Error(e.message ?: "Error")
            }
        }
    }

    fun nextPage() {
        if (carsUiState is CarsUiState.Success) {
            val state = carsUiState as CarsUiState.Success
            if (state.currentPage < state.totalPages - 1) {
                getCars(state.currentPage + 1)
            }
        }
    }

    fun previousPage() {
        if (carsUiState is CarsUiState.Success) {
            val state = carsUiState as CarsUiState.Success
            if (state.currentPage > 0) {
                getCars(state.currentPage - 1)
            }
        }
    }

    fun goToPage(page: Int) {
        getCars(page)
    }

    fun refreshCarsList() {
        getCars(currentPage)
    }
}