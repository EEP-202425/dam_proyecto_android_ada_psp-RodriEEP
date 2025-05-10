package com.example.volantum.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val score: Double,
    val cars: List<Car>?,
    val drivingSessions: List<DrivingSession>?
)