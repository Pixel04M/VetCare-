package com.example.myapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consultations")
data class ConsultationEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val petId: String,
    val vetId: String,
    val vetName: String,
    val type: String, // "CHAT" or "VIDEO_CALL"
    val status: String, // "PENDING", "ACTIVE", "COMPLETED", "CANCELLED"
    val startTime: Long,
    val endTime: Long?,
    val lastMessage: String?,
    val lastMessageTime: Long?
)
