package com.example.suryashakthi.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Networking layer for OpenAI API (GPT-4o mini).
 */
interface OpenAiApi {
    @POST("v1/chat/completions")
    suspend fun generateContent(
        @Header("Authorization") authHeader: String,
        @Body request: OpenAiRequest
    ): OpenAiResponse
}

data class OpenAiRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<ChatMessage>
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class OpenAiResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)
