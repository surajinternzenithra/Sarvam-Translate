package com.zenithra.sarvam_translate.ui.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zenithra.sarvam_translate.Repository.SttRepository
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

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onSourceLanguageSelected(language: Language) {
        _uiState.update { it.copy(sourceLanguage = language) }
    }

    fun onTargetLanguageSelected(language: Language) {
        _uiState.update { it.copy(targetLanguage = language) }
    }

    fun onSourceTextChange(text: String) {
        _uiState.update { it.copy(transcribedText = text) }
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
                    _uiState.update { it.copy(transcribedText = text, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
            AudioFileManager(getApplication()).deleteFile(file)
        }
    }

    fun clearText() {
        _uiState.update { it.copy(transcribedText = "") }
    }
}

data class MainUiState(
    val isRecording: Boolean = false,
    val isLoading: Boolean = false,
    val transcribedText: String = "",
    val errorMessage: String? = null,
    val sourceLanguage: Language = SUPPORTED_LANGUAGES[0],
    val targetLanguage: Language = SUPPORTED_LANGUAGES[1],
)