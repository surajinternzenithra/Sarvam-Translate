package com.zenithra.sarvam_translate.api


import com.zenithra.sarvam_translate.model.TtsRequest
import com.zenithra.sarvam_translate.model.TtsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TtsApiService {

    @POST("text-to-speech")
    suspend fun textToSpeech(
        @Body request: TtsRequest
    ): TtsResponse
}