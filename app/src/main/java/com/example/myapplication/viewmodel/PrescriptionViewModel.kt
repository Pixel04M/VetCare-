package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Medication
import com.example.myapplication.data.Prescription
import com.example.myapplication.data.network.ApiService
import com.example.myapplication.data.network.PrescriptionRequest
import com.example.myapplication.data.network.PrescriptionResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class PrescriptionViewModel : ViewModel() {
    private val _prescriptions = MutableStateFlow<List<Prescription>>(emptyList())
    val prescriptions: StateFlow<List<Prescription>> = _prescriptions.asStateFlow()

    private val supabaseAnonKey = "sb_publishable_dluFHcAJR-LfwJDNT0FICA_Ni3QGk0s"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("apikey", supabaseAnonKey)
                .addHeader("Authorization", "Bearer $supabaseAnonKey")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pmllnpycgoaerizzpjzt.supabase.co/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    fun loadPrescriptions(userId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getPrescriptions(userId = "eq.$userId")
                if (response.isSuccessful) {
                    _prescriptions.value = response.body()?.map { it.toPrescription() } ?: emptyList()
                }
            } catch (e: Exception) {}
        }
    }

    suspend fun addPrescription(
        userId: String,
        petId: String,
        consultationId: String?,
        medications: List<Medication>,
        instructions: String
    ): Boolean {
        return try {
            val medList = medications.map {
                mapOf(
                    "name" to it.name,
                    "dosage" to it.dosage,
                    "frequency" to it.frequency,
                    "duration" to it.duration
                )
            }
            val request = PrescriptionRequest(
                userId = userId,
                petId = petId,
                consultationId = consultationId,
                medications = medList,
                instructions = instructions
            )
            val response = apiService.addPrescription(request)
            if (response.isSuccessful) {
                loadPrescriptions(userId)
                true
            } else false
        } catch (e: Exception) { false }
    }

    private fun PrescriptionResponse.toPrescription(): Prescription {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = try { sdf.parse(createdAt)?.time ?: 0L } catch (e: Exception) { 0L }

        return Prescription(
            id = id,
            consultationId = consultationId ?: "",
            petId = petId,
            vetId = "vet_001",
            vetName = "Dr. Sarah Johnson",
            medications = medications.map {
                Medication(
                    name = it["name"] ?: "",
                    dosage = it["dosage"] ?: "",
                    frequency = it["frequency"] ?: "",
                    duration = it["duration"] ?: ""
                )
            },
            instructions = instructions,
            date = date,
            isDelivered = false
        )
    }
}
