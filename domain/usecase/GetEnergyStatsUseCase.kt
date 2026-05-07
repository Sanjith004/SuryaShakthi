package com.example.suryashakthi.domain.usecase

import com.example.suryashakthi.domain.model.EnergyData
import com.example.suryashakthi.domain.repository.EnergyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Encapsulates the logic for calculating aggregate energy statistics.
 */
class GetEnergyStatsUseCase @Inject constructor(
    private val repository: EnergyRepository
) {
    operator fun invoke(): Flow<EnergyStats> {
        return repository.getAllEnergyData().map { list ->
            val totalSolar = list.sumOf { it.solarGenerated.toDouble() }.toFloat()
            val totalCons = list.sumOf { it.consumption.toDouble() }.toFloat()
            val avgGreen = if (list.isEmpty()) 0 else list.map { it.greenScore }.average().toInt()
            
            EnergyStats(totalSolar, totalCons, avgGreen, list)
        }
    }
}

data class EnergyStats(
    val totalSolar: Float,
    val totalConsumption: Float,
    val averageGreenScore: Int,
    val history: List<EnergyData>
)
