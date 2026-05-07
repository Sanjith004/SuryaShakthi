package com.example.suryashakthi.domain.model

/**
 * Domain-level model for energy data.
 * Decoupled from database entities to allow independent changes.
 */
data class EnergyData(
    val id: Int,
    val date: String,
    val solarGenerated: Float,
    val consumption: Float,
    val aiInsight: String? = null
) {
    val netEnergy: Float get() = solarGenerated - consumption
    
    val greenScore: Int get() {
        if (consumption == 0f) return 100
        return ((solarGenerated / consumption) * 100).coerceIn(0f, 100f).toInt()
    }
}
