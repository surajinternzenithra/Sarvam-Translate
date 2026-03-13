package com.zenithra.sarvam_translate.model

import com.google.gson.annotations.SerializedName

data class SttResponse(
    val transcript: String
)

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
    @SerializedName("speaker") val speaker: String = "meera",
    @SerializedName("model") val model: String = "bulbul:v1",
    @SerializedName("pitch") val pitch: Float = 0f,
    @SerializedName("pace") val pace: Float = 1.0f,
    @SerializedName("loudness") val loudness: Float = 1.5f,
    @SerializedName("speech_sample_rate") val speechSampleRate: Int = 8000,
    @SerializedName("enable_preprocessing") val enablePreprocessing: Boolean = true
)

data class TtsResponse(
    @SerializedName("audios") val audios: List<String>,
    @SerializedName("request_id") val requestId: String? = null
)