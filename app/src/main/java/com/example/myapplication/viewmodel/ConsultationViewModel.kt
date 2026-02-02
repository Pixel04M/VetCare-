package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Consultation
import com.example.myapplication.data.ConsultationStatus
import com.example.myapplication.data.ConsultationType
import com.example.myapplication.data.network.ApiService
import com.example.myapplication.data.network.ConsultationRequest
import com.example.myapplication.data.network.ConsultationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class ConsultationViewModel : ViewModel() {
    private val _consultations = MutableStateFlow<List<Consultation>>(emptyList())
    val consultations: StateFlow<List<Consultation>> = _consultations.asStateFlow()

    private val supabaseAnonKey = "sb_publishable_dluFHcAJR-LfwJDNT0FICA_Ni3QGk0s"

    private var accessToken: String? = null

    private fun createClient(token: String?): OkHttpClient {
        val authToken = token ?: supabaseAnonKey
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apikey", supabaseAnonKey)
                    .addHeader("Authorization", "Bearer $authToken")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=representation")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    private fun getRetrofit(token: String?): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pmllnpycgoaerizzpjzt.supabase.co/")
            .client(createClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun setAccessToken(token: String?) {
        accessToken = token
    }

    private val apiService: ApiService
        get() = getRetrofit(accessToken).create(ApiService::class.java)

    fun loadConsultations(userId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getConsultations(userId = "eq.$userId")
                if (response.isSuccessful) {
                    _consultations.value = response.body()?.map { it.toConsultation() } ?: emptyList()
                }
            } catch (e: Exception) {}
        }
    }

    suspend fun startConsultation(userId: String, petId: String, type: ConsultationType): Boolean {
        return try {
            val request = ConsultationRequest(
                userId = userId,
                petId = petId,
                type = type.name
            )
            val response = apiService.startConsultation(request)
            if (response.isSuccessful) {
                loadConsultations(userId)
                true
            } else false
        } catch (e: Exception) { false }
    }

    private fun ConsultationResponse.toConsultation(): Consultation {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val startTime = try { sdf.parse(startedAt)?.time ?: 0L } catch (e: Exception) { 0L }
        val endTime = try { endedAt?.let { sdf.parse(it)?.time } } catch (e: Exception) { null }

        return Consultation(
            id = id,
            userId = userId,
            petId = petId,
            vetId = "vet_001",
            vetName = "Dr. Sarah Johnson",
            type = ConsultationType.valueOf(type.uppercase()),
            status = ConsultationStatus.valueOf(status.uppercase()),
            startTime = startTime,
            endTime = endTime,
            messages = emptyList()
        )
    }
}
