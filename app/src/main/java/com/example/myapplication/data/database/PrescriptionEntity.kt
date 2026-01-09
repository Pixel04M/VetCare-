package com.example.myapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prescriptions")
data class PrescriptionEntity(
    @PrimaryKey
    val id: String,
    val consultationId: String,
    val petId: String,
    val vetId: String,
    val vetName: String,
    val medicationsJson: String, // JSON string of medications list
    val instructions: String,
    val date: Long,
    val isDelivered: Boolean
)
