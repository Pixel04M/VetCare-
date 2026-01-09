package com.example.myapplication.data

data class Prescription(
    val id: String = "",
    val consultationId: String = "",
    val petId: String = "",
    val vetId: String = "",
    val vetName: String = "",
    val medications: List<Medication> = emptyList(),
    val instructions: String = "",
    val date: Long = System.currentTimeMillis(),
    val isDelivered: Boolean = false
)

data class Medication(
    val name: String = "",
    val dosage: String = "",
    val frequency: String = "",
    val duration: String = ""
)
