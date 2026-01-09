package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.OnlineVetHospitalApplication
import com.example.myapplication.data.Consultation
import com.example.myapplication.data.ConsultationStatus
import com.example.myapplication.data.ConsultationType
import com.example.myapplication.data.database.ConsultationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ConsultationViewModel(application: Application) : AndroidViewModel(application) {
    private val database = (application as OnlineVetHospitalApplication).database
    private val consultationDao = database.consultationDao()

    private val _consultations = MutableStateFlow<List<Consultation>>(emptyList())
    val consultations: StateFlow<List<Consultation>> = _consultations.asStateFlow()

    fun loadConsultations(userId: String) {
        viewModelScope.launch {
            consultationDao.getConsultationsByUserId(userId).collect { entities ->
                _consultations.value = entities.map { it.toConsultation() }
            }
        }
    }

    suspend fun startConsultation(
        userId: String,
        petId: String,
        type: ConsultationType
    ): String {
        val consultationId = UUID.randomUUID().toString()
        val entity = ConsultationEntity(
            id = consultationId,
            userId = userId,
            petId = petId,
            vetId = "vet_001", // Mock vet ID
            vetName = "Dr. Sarah Johnson",
            type = type.name,
            status = ConsultationStatus.ACTIVE.name,
            startTime = System.currentTimeMillis(),
            endTime = null,
            lastMessage = null,
            lastMessageTime = null
        )
        consultationDao.insertConsultation(entity)
        return consultationId
    }

    private fun ConsultationEntity.toConsultation() = Consultation(
        id = id,
        userId = userId,
        petId = petId,
        vetId = vetId,
        vetName = vetName,
        type = ConsultationType.valueOf(type),
        status = ConsultationStatus.valueOf(status),
        startTime = startTime,
        endTime = endTime,
        messages = emptyList()
    )
}
