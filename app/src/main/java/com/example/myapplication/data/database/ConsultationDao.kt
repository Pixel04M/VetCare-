package com.example.myapplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsultationDao {
    @Query("SELECT * FROM consultations WHERE userId = :userId ORDER BY startTime DESC")
    fun getConsultationsByUserId(userId: String): Flow<List<ConsultationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsultation(consultation: ConsultationEntity)

    @Query("UPDATE consultations SET status = :status, endTime = :endTime WHERE id = :consultationId")
    suspend fun updateConsultationStatus(consultationId: String, status: String, endTime: Long?)

    @Query("UPDATE consultations SET lastMessage = :message, lastMessageTime = :timestamp WHERE id = :consultationId")
    suspend fun updateLastMessage(consultationId: String, message: String, timestamp: Long)
}
