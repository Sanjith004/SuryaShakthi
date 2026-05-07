package com.example.suryashakthi.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AiApiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun getGeminiInsights(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}
