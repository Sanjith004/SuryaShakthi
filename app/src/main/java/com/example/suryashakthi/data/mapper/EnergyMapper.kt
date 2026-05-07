package com.example.suryashakthi.data.mapper

import com.example.suryashakthi.data.local.EnergyDataEntity
import com.example.suryashakthi.domain.model.EnergyData

/**
 * Extension functions to map between data entities and domain models.
 */
fun EnergyDataEntity.toDomain(): EnergyData {
    return EnergyData(
        id = id,
        date = date,
        solarGenerated = solarGenerated,
        consumption = consumption,
        aiInsight = aiInsight
    )
}

fun EnergyData.toEntity(): EnergyDataEntity {
    return EnergyDataEntity(
        id = id,
        date = date,
        solarGenerated = solarGenerated,
        consumption = consumption,
        aiInsight = aiInsight
    )
}
