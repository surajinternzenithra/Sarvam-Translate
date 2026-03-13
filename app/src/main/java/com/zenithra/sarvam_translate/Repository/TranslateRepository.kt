package com.zenithra.sarvam_translate.Repository

import com.zenithra.sarvam_translate.api.RetrofitClient
import com.zenithra.sarvam_translate.model.TranslationRequest
import com.zenithra.sarvam_translate.model.TranslationResponse

class TranslateRepository {

    private val api = RetrofitClient.translateService

    suspend fun translate(
        input: String,
        sourceLanguageCode: String,
        targetLanguageCode: String
    ): Result<TranslationResponse> {
        return try {
            val request = TranslationRequest(
                input = input,
                sourceLanguageCode = sourceLanguageCode,
                targetLanguageCode = targetLanguageCode
            )
            val response = api.translateText(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}