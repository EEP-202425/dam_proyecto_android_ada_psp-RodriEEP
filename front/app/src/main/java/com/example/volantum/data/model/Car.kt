package com.example.volantum.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    val id: Int,
    val plate: String,
    val brand: String,
    val model: String,
    val yearModel: Int,
    val image: String,
    val mileage: Float,
)
