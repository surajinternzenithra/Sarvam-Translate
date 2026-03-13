package com.zenithra.sarvam_translate.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zenithra.sarvam_translate.ui.ViewModel.MainViewModel
import com.zenithra.sarvam_translate.utils.SUPPORTED_LANGUAGES

data class Language(val displayName: String, val code: String)

// ─── Main Screen ──────────────────────────────────────────────────────────────

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var translatedText by remember { mutableStateOf("") }
    var selectedSourceLanguage by remember { mutableStateOf(SUPPORTED_LANGUAGES[0]) }
    var selectedTargetLanguage by remember { mutableStateOf(SUPPORTED_LANGUAGES[1]) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Translator",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // ── Error Message ─────────────────────────────────────────────────
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            // ── Source Text Section ───────────────────────────────────────────
            SourceTextSection(
                text = uiState.transcribedText,
                onTextChange = { /* allow manual edit if needed */ },
                isRecording = uiState.isRecording,
                isLoading = uiState.isLoading,
                onMicClick = {
                    if (uiState.isRecording) {
                        viewModel.stopRecording()
                    } else {
                        viewModel.startRecording()
                    }
                },
                onSpeakerClick = { /* trigger TTS for source text */ }
            )

            // ── Source Language Selector ──────────────────────────────────────
            LanguageSelector(
                label = "From",
                selectedLanguage = selectedSourceLanguage,
                languages = SUPPORTED_LANGUAGES,
                onLanguageSelected = { selectedSourceLanguage = it }
            )

            // ── Target Language Selector ──────────────────────────────────────
            LanguageSelector(
                label = "To",
                selectedLanguage = selectedTargetLanguage,
                languages = SUPPORTED_LANGUAGES,
                onLanguageSelected = { selectedTargetLanguage = it }
            )

            // ── Target Text Section ───────────────────────────────────────────
            TargetTextSection(
                text = translatedText,
                onSpeakerClick = { /* trigger TTS for translated text */ }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Translate Button ──────────────────────────────────────────────
            Button(
                onClick = { /* trigger translation */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = uiState.transcribedText.isNotBlank() && !uiState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Translate",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ─── Source Text Section ──────────────────────────────────────────────────────

@Composable
fun SourceTextSection(
    text: String,
    onTextChange: (String) -> Unit,
    isRecording: Boolean,
    isLoading: Boolean,
    onMicClick: () -> Unit,
    onSpeakerClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                placeholder = {
                    Text(
                        text = "Enter or speak text to translate...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                maxLines = 5
            )

            // Loading spinner inside text box top-right
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MicButton(
                isRecording = isRecording,
                onClick = onMicClick
            )

            IconButton(
                onClick = onSpeakerClick,
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.VolumeUp,
                    contentDescription = "Speak source text",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// ─── Target Text Section ──────────────────────────────────────────────────────

@Composable
fun TargetTextSection(
    text: String,
    onSpeakerClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            placeholder = {
                Text(
                    text = "Translation will appear here...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
            ),
            maxLines = 5
        )

        IconButton(
            onClick = onSpeakerClick,
            modifier = Modifier
                .size(48.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Filled.VolumeUp,
                contentDescription = "Speak translated text",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ─── Mic Button ───────────────────────────────────────────────────────────────

@Composable
fun MicButton(
    isRecording: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .border(
                width = 1.dp,
                color = if (isRecording) Color.Red else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        if (isRecording) {
            Icon(
                imageVector = Icons.Filled.Stop,
                contentDescription = "Stop recording",
                tint = Color.Red
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Mic,
                contentDescription = "Start recording",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// ─── Language Selector ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    label: String,
    selectedLanguage: Language,
    languages: List<Language>,
    onLanguageSelected: (Language) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedLanguage.displayName,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(text = language.displayName) },
                        onClick = {
                            onLanguageSelected(language)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun TranslatorScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}