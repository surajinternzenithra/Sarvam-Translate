package com.zenithra.sarvam_translate.api

import com.zenithra.sarvam_translate.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("api-subscription-key", Constants.API_KEY)
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val sttService: SttApiService by lazy {
        buildRetrofit(Constants.BASE_URL).create(SttApiService::class.java)
    }

    val translateService: TranslateApiService by lazy {
        buildRetrofit(Constants.BASE_URL).create(TranslateApiService::class.java)
    }

    val ttsService: TtsApiService by lazy {
        buildRetrofit(Constants.BASE_URL).create(TtsApiService::class.java)
    }
}