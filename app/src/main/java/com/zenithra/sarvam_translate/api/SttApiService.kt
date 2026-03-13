package com.zenithra.sarvam_translate.api

import com.zenithra.sarvam_translate.model.SttResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SttApiService {

    @Multipart
    @POST("speech-to-text")
    suspend fun transcribeAudio(
        @Part file: MultipartBody.Part,
        @Part("model") model: RequestBody,
        @Part("mode") mode: RequestBody,
        @Part("language_code") languageCode: RequestBody
    ): SttResponse
}