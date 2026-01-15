package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Pet
import com.example.myapplication.data.network.ApiService
import com.example.myapplication.data.network.PetRequest
import com.example.myapplication.data.network.PetResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PetViewModel : ViewModel() {
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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

    fun loadPets(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // query parameter for filtering by user_id
                val response = apiService.getPets(userId = "eq.$userId")
                if (response.isSuccessful) {
                    _pets.value = response.body()?.map { it.toPet() } ?: emptyList()
                    Log.d("PetViewModel", "Loaded ${_pets.value.size} pets")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("PetViewModel", "Failed to load pets: ${response.code()} - $errorBody")
                    _error.value = "Failed to load pets: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("PetViewModel", "Error loading pets", e)
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun addPet(userId: String, pet: Pet): Boolean {
        _error.value = null
        return try {
            val request = PetRequest(
                userId = userId,
                name = pet.name,
                breed = pet.breed,
                age = pet.age,
                species = pet.species,
                medicalHistory = pet.medicalHistory.ifEmpty { null },
                photoUri = pet.photoUri
            )
            Log.d("PetViewModel", "Adding pet: $request")
            val response = apiService.insertPet(request)
            if (response.isSuccessful) {
                Log.d("PetViewModel", "Pet added successfully")
                loadPets(userId) // refresh list
                true
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("PetViewModel", "Failed to add pet: ${response.code()} - $errorBody")
                _error.value = "Failed to add pet: ${response.code()}. $errorBody"
                false
            }
        } catch (e: Exception) {
            Log.e("PetViewModel", "Error adding pet", e)
            _error.value = "Network error: ${e.localizedMessage}"
            false
        }
    }

    fun clearError() {
        _error.value = null
    }

    private fun PetResponse.toPet() = Pet(
        id = id,
        userId = userId,
        name = name,
        breed = breed,
        age = age,
        photoUri = photoUri,
        medicalHistory = medicalHistory ?: "",
        species = species
    )
}
