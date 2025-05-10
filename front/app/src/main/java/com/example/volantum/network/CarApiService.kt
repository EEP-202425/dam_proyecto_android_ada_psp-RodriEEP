package com.example.volantum.network

import com.example.volantum.data.model.Car
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "http://192.168.1.121:8080"

// Configuración del serializador Json
private val json = Json { 
    ignoreUnknownKeys = true
    coerceInputValues = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface CarApiService {
    @GET("api/cars/user/4")
    suspend fun getCars(): List<Car>

    @GET("api/cars/{id}")
    suspend fun getCarsDetail(@Path(value = "id") id: Int ): Car
}

object CarApi {
    val retrofitService: CarApiService by lazy {
        retrofit.create(CarApiService::class.java)
    }
}




