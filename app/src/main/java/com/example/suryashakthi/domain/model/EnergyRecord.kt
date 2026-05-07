package com.example.suryashakthi.domain.model

import java.util.Date

/**
 * Immutable domain model representing a high-fidelity energy snapshot.
 */
data class EnergyRecord(
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val solarProduction: Float,    // in kWh
    val gridConsumption: Float,   // in kWh
    val batteryStorage: Float,    // in %
    val weatherCondition: String, // e.g., "Sunny", "Cloudy"
    val carbonReduced: Float = solarProduction * 0.43f // kg of CO2 saved
) {
    // Constant for electricity rate (e.g., $0.12 or ₹7.0 per unit)
    companion object {
        const val COST_PER_KWH = 7.0f 
    }

    val netEnergyFlow: Float get() = solarProduction - gridConsumption
    
    // Total savings: Solar produced * unit rate
    val savings: Float get() = solarProduction * COST_PER_KWH

    val efficiencyScore: Int get() {
        if (solarProduction + gridConsumption == 0f) return 0
        return ((solarProduction / (solarProduction + gridConsumption)) * 100).toInt().coerceIn(0, 100)
    }

    // Handles Export to Grid scenario
    val isExportingToGrid: Boolean get() = netEnergyFlow > 0
}
