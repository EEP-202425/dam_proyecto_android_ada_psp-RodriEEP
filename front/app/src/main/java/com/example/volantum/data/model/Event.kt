package com.example.volantum.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int,
    val timestamp: String,
    val latitude: Float,
    val longitude: Float,
    val type: EventType,
    val drivingSession: DrivingSession,
)