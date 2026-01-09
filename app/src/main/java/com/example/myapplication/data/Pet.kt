package com.example.myapplication.data

data class Pet(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val breed: String = "",
    val age: Int = 0,
    val photoUri: String? = null,
    val medicalHistory: String = "",
    val species: String = "Dog" // Dog, Cat, Bird, etc.
)
