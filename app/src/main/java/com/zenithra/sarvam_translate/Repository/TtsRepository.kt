package com.zenithra.sarvam_translate.Repository

import com.zenithra.sarvam_translate.api.RetrofitClient
import com.zenithra.sarvam_translate.model.TtsRequest
import com.zenithra.sarvam_translate.model.TtsResponse
import retrofit2.HttpException

class TtsRepository {

    private val api = RetrofitClient.ttsService

    suspend fun textToSpeech(
        text: String,
        targetLanguageCode: String,
    ): Result<TtsResponse> {
        return try {
            val request = TtsRequest(
                inputs = listOf(text),
                targetLanguageCode = targetLanguageCode,
            )
            val response = api.textToSpeech(request)
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}