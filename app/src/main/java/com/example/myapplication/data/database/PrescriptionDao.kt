package com.example.myapplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {
    @Query("SELECT * FROM prescriptions WHERE petId = :petId ORDER BY date DESC")
    fun getPrescriptionsByPetId(petId: String): Flow<List<PrescriptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrescription(prescription: PrescriptionEntity)

    @Query("UPDATE prescriptions SET isDelivered = :isDelivered WHERE id = :prescriptionId")
    suspend fun updateDeliveryStatus(prescriptionId: String, isDelivered: Boolean)
}
