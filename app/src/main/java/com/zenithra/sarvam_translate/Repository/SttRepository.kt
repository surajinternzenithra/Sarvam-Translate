package com.zenithra.sarvam_translate.Repository

import com.zenithra.sarvam_translate.api.RetrofitClient
import com.zenithra.sarvam_translate.utils.Constants
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class SttRepository {

    private val api = RetrofitClient.sttService

    suspend fun transcribeAudio(audioFile: File, languageCode: String): Result<String> {
        return try {

            val requestBody = audioFile.asRequestBody(Constants.AUDIO_MIME_TYPE.toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("file", audioFile.name, requestBody)

            val model = Constants.WHISPER_MODEL.toRequestBody("text/plain".toMediaTypeOrNull())
            val mode = "transcribe".toRequestBody("text/plain".toMediaTypeOrNull())
            val languageCode = languageCode.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = api.transcribeAudio(multipartBody, model, mode, languageCode)

            Result.success(response.transcript)
        }
        catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(e)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}