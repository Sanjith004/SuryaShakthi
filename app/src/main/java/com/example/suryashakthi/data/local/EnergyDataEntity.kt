package com.example.suryashakthi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "energy_data")
data class EnergyDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val solarGenerated: Float,
    val consumption: Float,
    val aiInsight: String? = null // Caching AI response as requested
)
