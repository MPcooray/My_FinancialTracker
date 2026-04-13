package com.example.my_financialtracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.my_financialtracker.data.local.entity.DetectedTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DetectedTransactionDao {
    @Query("SELECT * FROM detected_transactions WHERE status = 'PENDING' ORDER BY occurredAt DESC")
    fun observePending(): Flow<List<DetectedTransactionEntity>>

    @Query("SELECT * FROM detected_transactions WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): DetectedTransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: DetectedTransactionEntity)
}
