package com.example.volantum.network

import com.example.volantum.data.model.Car
import com.example.volantum.data.model.PageResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "http://10.0.2.2:8080"

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
    @GET("api/cars/user/1")
    suspend fun getCars(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): PageResponse<Car>

    @GET("api/cars/{id}")
    suspend fun getCarsDetail(@Path(value = "id") id: Int ): Car

    @POST("api/cars/user/1")
    suspend fun createCar(@Body car: Car): Car

    @PUT("api/cars/{id}")
    suspend fun updateCar(@Path(value = "id") id: Int, @Body car: Car): Car

    @DELETE("api/cars/{id}")
    suspend fun deleteCar(@Path(value = "id") id: Int):  Response<Unit>
}

object CarApi {
    val retrofitService: CarApiService by lazy {
        retrofit.create(CarApiService::class.java)
    }
}




