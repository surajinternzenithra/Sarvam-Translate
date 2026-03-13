package com.zenithra.sarvam_translate.utils.text_to_speech

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Base64

object AudioPlayer {

    private var audioTrack: AudioTrack? = null

    fun playBase64Wav(base64Audio: String) {
        stop()

        val wavBytes = Base64.decode(base64Audio, Base64.DEFAULT)
        val pcmData = wavBytes.copyOfRange(44, wavBytes.size)
        val sampleRate = 24000

        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(pcmData.size)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack?.write(pcmData, 0, pcmData.size)
        audioTrack?.play()
    }

    fun stop() {
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
}