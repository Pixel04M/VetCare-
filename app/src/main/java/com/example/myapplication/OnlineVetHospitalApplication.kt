package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.database.VetHospitalDatabase

class OnlineVetHospitalApplication : Application() {
    val database by lazy { VetHospitalDatabase.getDatabase(this) }
}
