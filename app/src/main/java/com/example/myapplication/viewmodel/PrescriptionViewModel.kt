package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.OnlineVetHospitalApplication
import com.example.myapplication.data.Medication
import com.example.myapplication.data.Prescription
import com.example.myapplication.data.database.PrescriptionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class PrescriptionViewModel(application: Application) : AndroidViewModel(application) {
    private val database = (application as OnlineVetHospitalApplication).database
    private val prescriptionDao = database.prescriptionDao()

    private val _prescriptions = MutableStateFlow<List<Prescription>>(emptyList())
    val prescriptions: StateFlow<List<Prescription>> = _prescriptions.asStateFlow()

    fun loadPrescriptions(petId: String) {
        viewModelScope.launch {
            prescriptionDao.getPrescriptionsByPetId(petId).collect { entities ->
                _prescriptions.value = entities.map { it.toPrescription() }
            }
        }
    }

    suspend fun addPrescription(
        consultationId: String,
        petId: String,
        medications: List<Medication>,
        instructions: String
    ): Boolean {
        return try {
            val prescriptionId = UUID.randomUUID().toString()
            val medicationsJson = medicationsToJson(medications)
            val entity = PrescriptionEntity(
                id = prescriptionId,
                consultationId = consultationId,
                petId = petId,
                vetId = "vet_001",
                vetName = "Dr. Sarah Johnson",
                medicationsJson = medicationsJson,
                instructions = instructions,
                date = System.currentTimeMillis(),
                isDelivered = false
            )
            prescriptionDao.insertPrescription(entity)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun medicationsToJson(medications: List<Medication>): String {
        val jsonArray = JSONArray()
        medications.forEach { med ->
            val jsonObject = JSONObject()
            jsonObject.put("name", med.name)
            jsonObject.put("dosage", med.dosage)
            jsonObject.put("frequency", med.frequency)
            jsonObject.put("duration", med.duration)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    private fun jsonToMedications(json: String): List<Medication> {
        val medications = mutableListOf<Medication>()
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            medications.add(
                Medication(
                    name = obj.getString("name"),
                    dosage = obj.getString("dosage"),
                    frequency = obj.getString("frequency"),
                    duration = obj.getString("duration")
                )
            )
        }
        return medications
    }

    private fun PrescriptionEntity.toPrescription() = Prescription(
        id = id,
        consultationId = consultationId,
        petId = petId,
        vetId = vetId,
        vetName = vetName,
        medications = jsonToMedications(medicationsJson),
        instructions = instructions,
        date = date,
        isDelivered = isDelivered
    )
}
