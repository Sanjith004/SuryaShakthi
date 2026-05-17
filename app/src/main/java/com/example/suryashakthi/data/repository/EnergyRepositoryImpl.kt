package com.example.suryashakthi.data.repository

import com.example.suryashakthi.BuildConfig
import com.example.suryashakthi.data.local.EnergyDao
import com.example.suryashakthi.data.local.toDomain
import com.example.suryashakthi.data.local.toEntity
import com.example.suryashakthi.data.remote.*
import com.example.suryashakthi.domain.model.EnergyRecord
import com.example.suryashakthi.domain.repository.EnergyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnergyRepositoryImpl @Inject constructor(
    private val dao: EnergyDao,
    private val api: GeminiApi
) : EnergyRepository {

    override fun getHistoricalRecords(): Flow<List<EnergyRecord>> {
        return dao.getAllRecords().map { entities -> 
            entities.map { it.toDomain() } 
        }
    }

    override suspend fun saveRecord(record: EnergyRecord) {
        dao.insertRecord(record.toEntity())
    }

    override suspend fun deleteRecord(record: EnergyRecord) {
        dao.deleteRecord(record.toEntity())
    }

    override suspend fun fetchAiInsights(prompt: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey == "null" || apiKey.isBlank()) {
            throw Exception("Gemini API Key is missing. Please add GEMINI_API_KEY to your local.properties file.")
        }

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt))))
        )
        // Using the secure key from BuildConfig (invisible to user)
        val response = api.generateContent(apiKey, request)
        return response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: "No insights available."
    }
}
