package com.zenithra.sarvam_translate.api


import retrofit2.http.Body
import retrofit2.http.POST

interface TtsApiService {

    @POST("text-to-speech")
    suspend fun textToSpeech(
        @Body request: TtsRequest
    ): TtsResponse
}