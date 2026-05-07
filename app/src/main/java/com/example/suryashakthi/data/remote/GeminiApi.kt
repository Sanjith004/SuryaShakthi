package com.example.suryashakthi.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Enterprise networking layer for Gemini 1.5 Flash.
 */
interface GeminiApi {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

data class GeminiRequest(val contents: List<Content>)
data class Content(val parts: List<Part>)
data class Part(val text: String)

data class GeminiResponse(val candidates: List<Candidate>)
data class Candidate(val content: Content)
