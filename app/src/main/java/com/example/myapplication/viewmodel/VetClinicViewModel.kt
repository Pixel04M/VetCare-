package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.VetClinic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.*

class VetClinicViewModel : ViewModel() {
    private val _nearbyClinics = MutableStateFlow<List<VetClinic>>(emptyList())
    val nearbyClinics: StateFlow<List<VetClinic>> = _nearbyClinics

    // Mock data - replace with actual API call
    private val mockClinics = listOf(
        VetClinic(
            id = "1",
            name = "Happy Paws Veterinary Clinic",
            address = "123 Main Street",
            latitude = 40.7128,
            longitude = -74.0060,
            phone = "+1-555-0101",
            rating = 4.8f,
            isEmergency = false
        ),
        VetClinic(
            id = "2",
            name = "Emergency Pet Care Center",
            address = "456 Oak Avenue",
            latitude = 40.7580,
            longitude = -73.9855,
            phone = "+1-555-0102",
            rating = 4.9f,
            isEmergency = true
        ),
        VetClinic(
            id = "3",
            name = "City Animal Hospital",
            address = "789 Park Boulevard",
            latitude = 40.7505,
            longitude = -73.9934,
            phone = "+1-555-0103",
            rating = 4.7f,
            isEmergency = false
        )
    )

    fun findNearbyClinics(userLat: Double, userLon: Double) {
        viewModelScope.launch {
            val clinicsWithDistance = mockClinics.map { clinic ->
                val distance = calculateDistance(
                    userLat, userLon,
                    clinic.latitude, clinic.longitude
                )
                clinic.copy(distance = distance)
            }.sortedBy { it.distance }
            _nearbyClinics.value = clinicsWithDistance
        }
    }

    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 6371.0 // km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }
}
