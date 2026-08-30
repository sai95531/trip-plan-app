package com.example.ui.translation

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {
    private val tag = "TtsManager"
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.language = Locale.ENGLISH
        } else {
            Log.e(tag, "TextToSpeech initialization failed with status $status")
        }
    }

    fun speak(text: String, languageTag: String = "en-US") {
        if (!isInitialized || tts == null) {
            Log.w(tag, "TTS not initialized yet")
            return
        }

        try {
            val locale = when (languageTag.lowercase()) {
                "hi", "hi-in" -> Locale("hi", "IN")
                "ta", "ta-in" -> Locale("ta", "IN")
                "te", "te-in" -> Locale("te", "IN")
                "kn", "kn-in" -> Locale("kn", "IN")
                "ml", "ml-in" -> Locale("ml", "IN")
                "bn", "bn-in" -> Locale("bn", "IN")
                "mr", "mr-in" -> Locale("mr", "IN")
                "gu", "gu-in" -> Locale("gu", "IN")
                "pa", "pa-in" -> Locale("pa", "IN")
                "ja", "ja-jp" -> Locale.JAPANESE
                "fr", "fr-fr" -> Locale.FRENCH
                "es", "es-es" -> Locale("es", "ES")
                "de", "de-de" -> Locale.GERMAN
                "it", "it-it" -> Locale.ITALIAN
                "th", "th-th" -> Locale("th", "TH")
                "ar", "ar-sa" -> Locale("ar", "SA")
                else -> Locale.ENGLISH
            }

            tts?.language = locale
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "UTTERANCE_ID_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            Log.e(tag, "Error speaking text", e)
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
