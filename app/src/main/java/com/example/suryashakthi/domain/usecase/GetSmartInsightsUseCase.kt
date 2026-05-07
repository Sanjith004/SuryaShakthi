package com.example.suryashakthi.domain.usecase

import com.example.suryashakthi.domain.repository.EnergyRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Encapsulates the logic for aggregating historical data and requesting 
 * AI-driven analysis from the Gemini LLM.
 */
class GetSmartInsightsUseCase @Inject constructor(
    private val repository: EnergyRepository
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            val history = repository.getHistoricalRecords().first()
            
            val analysisRequest = if (history.isEmpty()) {
                "Act as an expert energy consultant. Provide 3 general, high-impact tips for a new solar panel owner to maximize their ROI and sustainability."
            } else {
                buildPrompt(history.takeLast(7))
            }
            
            val insight = repository.fetchAiInsights(analysisRequest)
            Result.success(insight)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildPrompt(records: List<com.example.suryashakthi.domain.model.EnergyRecord>): String {
        val summary = records.joinToString("; ") { 
            "Date: ${it.timestamp}, Solar: ${it.solarProduction}kWh, Grid: ${it.gridConsumption}kWh" 
        }
        return "Act as an expert energy consultant. Analyze these solar logs and provide 3 actionable, high-impact strategies to maximize ROI and sustainability: $summary"
    }
}
