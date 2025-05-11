package com.example.volantum.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DrivingSession(
    val id: Int,
    val description: String?,
    val startTime: String,
    val endTime: String?,
    val distance: Float?,
    val duration: String?,
    val averageSpeed: Float?,
    val score: Float?,
    val car: Car,
    val events: List<Event> = emptyList()
)
