package com.example.volantum.data.model

data class Event(
    val id: Int,
    val timestamp: String,
    val latitude: Float,
    val longitude: Float,
    val type: EventType,
    val drivingSession: DrivingSession,
)