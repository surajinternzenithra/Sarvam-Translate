package com.zenithra.sarvam_translate.model

import com.google.gson.annotations.SerializedName

data class SttResponse(
    val transcript: String
)

data class Language(val displayName: String, val code: String)


 // Translation
data class TranslationRequest(
    @SerializedName("input") val input: String,
    @SerializedName("source_language_code") val sourceLanguageCode: String,
    @SerializedName("target_language_code") val targetLanguageCode: String,
    @SerializedName("speaker_gender") val speakerGender: String = "Male",
    @SerializedName("mode") val mode: String = "formal",
    @SerializedName("model") val model: String = "mayura:v1",
    @SerializedName("enable_preprocessing") val enablePreprocessing: Boolean = true,
    @SerializedName("output_script") val outputScript: String? = null,
    @SerializedName("numerals_format") val numeralsFormat: String? = "international"
)

data class TranslationResponse(
    @SerializedName("request_id") val requestId: String? = null,
    @SerializedName("translated_text") val translatedText: String,
    @SerializedName("source_language_code") val sourceLanguageCode: String? = null
)

// TTS
data class TtsRequest(
    @SerializedName("inputs") val inputs: List<String>,
    @SerializedName("target_language_code") val targetLanguageCode: String,
    @SerializedName("speaker") val speaker: String = "shubh",
    @SerializedName("model") val model: String = "bulbul:v3",
    @SerializedName("pace") val pace: Float = 1.0f,
    @SerializedName("speech_sample_rate") val speechSampleRate: Int = 24000,
    @SerializedName("enable_preprocessing") val enablePreprocessing: Boolean = true
)

data class TtsResponse(
    @SerializedName("audios") val audios: List<String>,
    @SerializedName("request_id") val requestId: String? = null
)