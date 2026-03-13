package com.zenithra.sarvam_translate.api


import com.zenithra.sarvam_translate.model.TranslationRequest
import com.zenithra.sarvam_translate.model.TranslationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslateApiService {

    @POST("translate")
    suspend fun translateText(
        @Body request: TranslationRequest
    ): TranslationResponse
}