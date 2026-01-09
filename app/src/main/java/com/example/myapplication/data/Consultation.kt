package com.example.myapplication.data

import java.util.Date

data class Consultation(
    val id: String = "",
    val userId: String = "",
    val petId: String = "",
    val vetId: String = "",
    val vetName: String = "",
    val type: ConsultationType = ConsultationType.CHAT,
    val status: ConsultationStatus = ConsultationStatus.PENDING,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val messages: List<ChatMessage> = emptyList()
)

enum class ConsultationType {
    CHAT, VIDEO_CALL
}

enum class ConsultationStatus {
    PENDING, ACTIVE, COMPLETED, CANCELLED
}

data class ChatMessage(
    val id: String = "",
    val consultationId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val isFromVet: Boolean = false,
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
