package com.example.myapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val name: String,
    val breed: String,
    val age: Int,
    val photoUri: String?,
    val medicalHistory: String,
    val species: String
)
