package com.zenithra.sarvam_translate.ui.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zenithra.sarvam_translate.Repository.SttRepository
import com.zenithra.sarvam_translate.Repository.TranslateRepository
import com.zenithra.sarvam_translate.ui.Language
import com.zenithra.sarvam_translate.utils.AudioFileManager
import com.zenithra.sarvam_translate.utils.AudioRecorder
import com.zenithra.sarvam_translate.utils.SUPPORTED_LANGUAGES
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val audioRecorder = AudioRecorder(application)
    private val repository = SttRepository()
    private val translateRepository = TranslateRepository()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onSourceLanguageSelected(language: Language) {
        _uiState.update { it.copy(sourceLanguage = language) }
    }

    fun onTargetLanguageSelected(language: Language) {
        _uiState.update { it.copy(targetLanguage = language) }
    }

    fun onSourceTextChange(text: String) {
        _uiState.update { it.copy(sourceText = text) }
    }

    fun onTargetTextChange(text: String) {
        _uiState.update { it.copy(translatedText = text) }
    }

    fun startRecording() {
        if (_uiState.value.isRecording) return
        audioRecorder.start()
        _uiState.update { it.copy(isRecording = true, errorMessage = null) }
    }

    fun stopRecording() {
        if (!_uiState.value.isRecording) return
        audioRecorder.stop()
        _uiState.update { it.copy(isRecording = false, isLoading = true) }
        transcribeAudio()
    }

    private fun transcribeAudio() {
        val file = audioRecorder.currentFile ?: return

        viewModelScope.launch {
            val result = repository.transcribeAudio(file, _uiState.value.sourceLanguage.code)
            result
                .onSuccess { text ->
                    _uiState.update { it.copy(sourceText = text, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
            AudioFileManager(getApplication()).deleteFile(file)
        }
    }

    fun translate() {
        val state = _uiState.value
        if (state.sourceText.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true, errorMessage = null) }

            Log.d("surajjj", "Translating from ${state.sourceLanguage.code} to ${state.targetLanguage.code} with input: ${state.sourceText}")

            val result = translateRepository.translate(
                input = state.sourceText,
                sourceLanguageCode = state.sourceLanguage.code,
                targetLanguageCode = state.targetLanguage.code
            )

            result
                .onSuccess { response ->
                    Log.d("surajjj", "Translation successful: ${response.translatedText}")
                    _uiState.update { it.copy(translatedText = response.translatedText, isTranslating = false) }
                }
                .onFailure { error ->
                    Log.e("surajjj", "Translation failed: ${error.message}")
                    _uiState.update { it.copy(errorMessage = error.message, isTranslating = false) }
                }
        }
    }

}

data class MainUiState(
    val isRecording: Boolean = false,
    val isLoading: Boolean = false,
    val sourceText: String = "",
    val errorMessage: String? = null,
    val sourceLanguage: Language = SUPPORTED_LANGUAGES[0],
    val targetLanguage: Language = SUPPORTED_LANGUAGES[1],
    val translatedText: String = "",
    val isTranslating: Boolean = false,
)