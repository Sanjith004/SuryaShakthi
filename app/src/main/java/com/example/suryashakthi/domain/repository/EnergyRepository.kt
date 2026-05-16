package com.example.suryashakthi.domain.repository

import com.example.suryashakthi.domain.model.EnergyRecord
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Energy Repository. 
 * Allows the domain layer to request data without knowing about Room or Retrofit.
 */
interface EnergyRepository {
    fun getHistoricalRecords(): Flow<List<EnergyRecord>>
    suspend fun saveRecord(record: EnergyRecord)
    suspend fun deleteRecord(record: EnergyRecord)
    suspend fun fetchAiInsights(prompt: String): String
}
