package com.example.volantum.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EventType(
    val id: Int,
    val name: String,
    val description: String,
    val severity: String
)