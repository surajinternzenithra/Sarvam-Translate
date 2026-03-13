package com.zenithra.sarvam_translate.utils

import com.zenithra.sarvam_translate.ui.Language

val SUPPORTED_LANGUAGES = listOf(
    Language("English", "en-IN"),
    Language("Hindi", "hi-IN"),
    Language("Bengali", "bn-IN"),
    Language("Marathi", "mr-IN"),
    Language("Kannada", "kn-IN"),
    Language("Tamil", "ta-IN"),
    Language("Telugu", "te-IN")
)

object Constants {
    const val BASE_URL = "https://api.sarvam.ai/"
    const val WHISPER_MODEL = "saaras:v3"
    const val API_KEY = "sk_fzulcn1z_WzfcKV8FZSEgkwqiw3UHap8j"
    const val AUDIO_MIME_TYPE = "audio/x-m4a"
    const val LANG_CODE = "hi-IN"

    // other api key
    // sk_b8p65og0_HMR51IL5Lt2n7iOSPm0aTu7B
}