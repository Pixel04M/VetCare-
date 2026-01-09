package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.OnlineVetHospitalApplication
import com.example.myapplication.data.Pet
import com.example.myapplication.data.database.PetEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PetViewModel(application: Application) : AndroidViewModel(application) {
    private val database = (application as OnlineVetHospitalApplication).database
    private val petDao = database.petDao()

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()

    fun loadPets(userId: String) {
        viewModelScope.launch {
            petDao.getPetsByUserId(userId).collect { entities ->
                _pets.value = entities.map { it.toPet() }
            }
        }
    }

    suspend fun addPet(userId: String, pet: Pet): Boolean {
        return try {
            val entity = PetEntity(
                id = UUID.randomUUID().toString(),
                userId = userId,
                name = pet.name,
                breed = pet.breed,
                age = pet.age,
                photoUri = pet.photoUri,
                medicalHistory = pet.medicalHistory,
                species = pet.species
            )
            petDao.insertPet(entity)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deletePet(petId: String) {
        petDao.deletePet(petId)
    }

    private fun PetEntity.toPet() = Pet(
        id = id,
        userId = userId,
        name = name,
        breed = breed,
        age = age,
        photoUri = photoUri,
        medicalHistory = medicalHistory,
        species = species
    )
}
