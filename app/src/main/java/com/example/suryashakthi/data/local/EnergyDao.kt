package com.example.suryashakthi.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EnergyDao {
    @Query("SELECT * FROM energy_metrics ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<EnergyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: EnergyEntity)

    @Query("SELECT SUM(solarProduction) FROM energy_metrics")
    suspend fun getTotalSolarProduction(): Float?
}
