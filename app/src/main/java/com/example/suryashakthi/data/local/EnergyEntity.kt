package com.example.suryashakthi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.suryashakthi.domain.model.EnergyRecord

@Entity(tableName = "energy_metrics")
data class EnergyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val solarProduction: Float,
    val gridConsumption: Float,
    val batteryStorage: Float,
    val weatherCondition: String
)

/**
 * Mappers to keep the domain layer isolated from database details.
 */
fun EnergyEntity.toDomain() = EnergyRecord(
    id = id,
    timestamp = timestamp,
    solarProduction = solarProduction,
    gridConsumption = gridConsumption,
    batteryStorage = batteryStorage,
    weatherCondition = weatherCondition
)

fun EnergyRecord.toEntity() = EnergyEntity(
    timestamp = timestamp,
    solarProduction = solarProduction,
    gridConsumption = gridConsumption,
    batteryStorage = batteryStorage,
    weatherCondition = weatherCondition
)
