package com.example.myapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PetEntity::class, ConsultationEntity::class, PrescriptionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VetHospitalDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun consultationDao(): ConsultationDao
    abstract fun prescriptionDao(): PrescriptionDao

    companion object {
        @Volatile
        private var INSTANCE: VetHospitalDatabase? = null

        fun getDatabase(context: Context): VetHospitalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VetHospitalDatabase::class.java,
                    "vet_hospital_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
