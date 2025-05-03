package com.example.volantum.data.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val score: Float,
    val cars: List<Car>,
    val drivingSessions: List<DrivingSession>
)