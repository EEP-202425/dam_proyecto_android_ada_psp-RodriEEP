package com.example.volantum.network

import com.example.volantum.data.model.Car
import com.example.volantum.data.model.DrivingSession
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

interface DrivingSessionApiService {
    @GET("api/sessions/user/4")
    suspend fun getDrivingSessions(): List<DrivingSession>

    @GET("api/sessions/{id}")
    suspend fun getDrivingSessionDetail(@Path(value = "id") id: Int ): DrivingSession
}

object DrivingSessionApi {
    val retrofitService: DrivingSessionApiService by lazy {
        retrofit.create(DrivingSessionApiService::class.java)
    }
}




