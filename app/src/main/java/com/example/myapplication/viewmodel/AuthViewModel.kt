package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.network.ApiService
import com.example.myapplication.data.network.AuthResponse
import com.example.myapplication.data.network.LoginRequest
import com.example.myapplication.data.network.RegisterRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

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

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = apiService.login(request = LoginRequest(email, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.access_token != null) {
                        _isLoggedIn.value = true
                        _currentUser.value = body.user?.id ?: email
                        _accessToken.value = body.access_token
                        _userRole.value = (body.user?.user_metadata?.get("role") as? String) ?: "customer"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    parseError(errorBody, response.code())
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(name: String, email: String, password: String, phone: String, role: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _registrationSuccess.value = false
            try {
                val request = RegisterRequest(email, password, mapOf("name" to name, "phone" to phone, "role" to role))
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    _registrationSuccess.value = true
                    _error.value = "Vertify ur account"
                } else {
                    val errorBody = response.errorBody()?.string()
                    parseError(errorBody, response.code())
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parseError(errorBody: String?, responseCode: Int) {
        val errorObj = try {
            Gson().fromJson(errorBody, AuthResponse::class.java)
        } catch (e: Exception) { null }
        
        // Supabase sometimes puts the message in different fields
        val msg = errorObj?.error_description 
            ?: errorObj?.error 
            ?: errorObj?.message 
            ?: errorObj?.msg 
            ?: "Action failed"
        
        _error.value = when {
            msg.contains("credentials", true) || msg.contains("invalid", true) -> 
                "there is no account such like that"
            msg.contains("confirmed", true) || msg.contains("verify", true) -> 
                "Vertify ur account"
            responseCode == 400 && msg == "Action failed" ->
                "there is no account such like that"
            else -> msg
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
        _accessToken.value = null
        _userRole.value = null
    }

    fun clearError() { _error.value = null }
    fun resetRegistrationSuccess() { _registrationSuccess.value = false }
}
