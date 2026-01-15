package com.example.myapplication.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// --- Auth Data Classes ---
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String, val data: Map<String, String>? = null)
data class UserResponse(val id: String, val email: String?, val user_metadata: Map<String, Any>? = null)
data class AuthResponse(val access_token: String? = null, val user: UserResponse? = null, val error: String? = null, val error_description: String? = null, val message: String? = null, val msg: String? = null)

// --- Pets Data Classes ---
data class PetRequest(
    @SerializedName("user_id") val userId: String,
    val name: String,
    val breed: String,
    val age: Int,
    val species: String,
    @SerializedName("medical_history") val medicalHistory: String? = null,
    @SerializedName("photo_uri") val photoUri: String? = null
)

data class PetResponse(
    val id: String,
    @SerializedName("user_id") val userId: String,
    val name: String,
    val breed: String,
    val age: Int,
    val species: String,
    @SerializedName("medical_history") val medicalHistory: String?,
    @SerializedName("photo_uri") val photoUri: String?
)

// --- Consultations Data Classes ---
data class ConsultationRequest(
    @SerializedName("pet_id") val petId: String,
    @SerializedName("user_id") val userId: String,
    val type: String,
    val status: String = "active"
)

data class ConsultationResponse(
    val id: String,
    @SerializedName("pet_id") val petId: String,
    @SerializedName("user_id") val userId: String,
    val type: String,
    val status: String,
    @SerializedName("started_at") val startedAt: String,
    @SerializedName("ended_at") val endedAt: String?
)

// --- Prescriptions Data Classes ---
data class PrescriptionRequest(
    @SerializedName("consultation_id") val consultationId: String?,
    @SerializedName("pet_id") val petId: String,
    @SerializedName("user_id") val userId: String,
    val medications: List<Map<String, String>>,
    val instructions: String
)

data class PrescriptionResponse(
    val id: String,
    @SerializedName("consultation_id") val consultationId: String?,
    @SerializedName("pet_id") val petId: String,
    @SerializedName("user_id") val userId: String,
    val medications: List<Map<String, String>>,
    val instructions: String,
    @SerializedName("created_at") val createdAt: String
)

interface ApiService {
    @POST("auth/v1/token")
    suspend fun login(@Query("grant_type") grantType: String = "password", @Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/v1/signup")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    // Pets
    @POST("rest/v1/pets")
    suspend fun insertPet(@Body pet: PetRequest): Response<Unit>

    @GET("rest/v1/pets")
    suspend fun getPets(@Query("user_id") userId: String, @Query("select") select: String = "*"): Response<List<PetResponse>>

    // Consultations
    @POST("rest/v1/consultations")
    suspend fun startConsultation(@Body consultation: ConsultationRequest): Response<Unit>

    @GET("rest/v1/consultations")
    suspend fun getConsultations(@Query("user_id") userId: String, @Query("select") select: String = "*"): Response<List<ConsultationResponse>>

    // Prescriptions
    @POST("rest/v1/prescriptions")
    suspend fun addPrescription(@Body prescription: PrescriptionRequest): Response<Unit>

    @GET("rest/v1/prescriptions")
    suspend fun getPrescriptions(@Query("user_id") userId: String, @Query("select") select: String = "*"): Response<List<PrescriptionResponse>>
}
