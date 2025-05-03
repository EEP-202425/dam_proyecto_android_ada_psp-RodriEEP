package com.example.volantum.data.model

data class DrivingSession(
    val id: Int,
    val startTime: String,
    val endTime: String?,
    val distance: Float,
    val score: Float,
    val user: User,
    val car: Car,
    val events: List<Event> = emptyList()
)
