package com.zenithra.sarvam_translate.api

import android.view.translation.TranslationRequest
import android.view.translation.TranslationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslateApiService {

    @POST("translate")
    suspend fun translateText(
        @Body request: TranslationRequest
    ): TranslationResponse
}