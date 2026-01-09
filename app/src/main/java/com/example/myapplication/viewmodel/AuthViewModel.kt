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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.token != null || body?.success == true) {
                        _isLoggedIn.value = true
                        _currentUser.value = body?.user?.id ?: email
                    } else {
                        _error.value = body?.message ?: "Login failed"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = try {
                        Gson().fromJson(errorBody, AuthResponse::class.java)
                    } catch (e: Exception) { null }
                    _error.value = errorResponse?.message ?: "Login failed (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(name: String, email: String, password: String, phone: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _registrationSuccess.value = false
            try {
                val response = apiService.register(RegisterRequest(name, email, password, phone))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.token != null || body?.success == true) {
                        _registrationSuccess.value = true
                        // Note: We don't set _isLoggedIn to true here because the user 
                        // wants to go back to login page instead of home.
                    } else {
                        _error.value = body?.message ?: "Registration failed"
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = try {
                        Gson().fromJson(errorBody, AuthResponse::class.java)
                    } catch (e: Exception) { null }
                    _error.value = errorResponse?.message ?: "Registration failed (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
    }

    fun clearError() {
        _error.value = null
    }

    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }
}
