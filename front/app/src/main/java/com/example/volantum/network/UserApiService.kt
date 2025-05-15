package com.example.volantum.network

import com.example.volantum.data.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

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

interface UserApiService {
    @GET("api/users/4")
    suspend fun getUser(): User
}

object UserApi {
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}




