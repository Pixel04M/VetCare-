package com.example.myapplication.data

data class VetClinic(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val phone: String = "",
    val rating: Float = 0f,
    val isEmergency: Boolean = false,
    val distance: Double = 0.0 // in kilometers
)
