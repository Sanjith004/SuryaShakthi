package com.example.suryashakthi.data.repository

import com.example.suryashakthi.BuildConfig
import com.example.suryashakthi.data.local.EnergyDao
import com.example.suryashakthi.data.local.toDomain
import com.example.suryashakthi.data.local.toEntity
import com.example.suryashakthi.data.remote.ChatMessage
import com.example.suryashakthi.data.remote.OpenAiApi
import com.example.suryashakthi.data.remote.OpenAiRequest
import com.example.suryashakthi.domain.model.EnergyRecord
import com.example.suryashakthi.domain.repository.EnergyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnergyRepositoryImpl @Inject constructor(
    private val dao: EnergyDao,
    private val api: OpenAiApi
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
        val request = OpenAiRequest(
            messages = listOf(ChatMessage(role = "user", content = prompt))
        )
        // Using the secure key from BuildConfig (invisible to user)
        val response = api.generateContent("Bearer ${BuildConfig.OPENAI_API_KEY}", request)
        return response.choices.firstOrNull()?.message?.content
            ?: "No insights available."
    }
}
