package com.example.volantum.data.model

data class Car(
    val id: Int,
    val plate: String,
    val brand: String,
    val model: String,
    val yearModel: Int,
    val image: String,
    val mileage: Float,
    val user: User,
    val drivingSessions: List<DrivingSession>
)
