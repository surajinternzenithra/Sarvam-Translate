package com.zenithra.sarvam_translate.utils

import android.content.Context
import java.io.File

class AudioFileManager(private val context: Context) {

    fun createAudioFile(): File {
        val fileName = "recording_${System.currentTimeMillis()}.wav"
        return File(context.cacheDir, fileName)
    }

    fun deleteFile(file: File) {
        if (file.exists()) file.delete()
    }

//    fun clearAllRecordings() {
//        context.cacheDir.listFiles()
//            ?.filter { it.name.endsWith(".m4a") }
//            ?.forEach { it.delete() }
//    }
}