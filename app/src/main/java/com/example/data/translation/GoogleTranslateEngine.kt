package com.example.data.translation

import android.util.Log
import com.example.data.model.TranslationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

object GoogleTranslateEngine {
    private const val TAG = "GoogleTranslateEngine"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(6, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    suspend fun translate(
        text: String,
        sourceLangCode: String,
        targetLangCode: String,
        sourceLangName: String,
        targetLangName: String
    ): Result<TranslationResult> = withContext(Dispatchers.IO) {
        val query = text.trim()
        if (query.isBlank()) {
            return@withContext Result.success(
                TranslationResult(
                    originalText = text,
                    translatedText = "",
                    sourceLang = sourceLangName,
                    targetLang = targetLangName,
                    romanizedPronunciation = "",
                    culturalEtiquetteTip = "Enter text to translate"
                )
            )
        }

        try {
            // Map any special language codes if necessary
            val sl = if (sourceLangCode.isBlank() || sourceLangCode == "auto") "auto" else sourceLangCode
            val tl = targetLangCode.ifBlank { "en" }

            val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.name())
            val url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=$sl&tl=$tl&dt=t&dt=rm&q=$encoded"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Android; Mobile; rv:109.0) Gecko/109.0 Firefox/119.0")
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w(TAG, "Google Translate HTTP request failed: ${response.code}")
                return@withContext Result.failure(Exception("HTTP error ${response.code}"))
            }

            val body = response.body?.string() ?: ""
            if (body.isBlank()) {
                return@withContext Result.failure(Exception("Empty translation response"))
            }

            val rootArray = JSONArray(body)
            val sentencesArray = rootArray.optJSONArray(0)

            val translatedBuilder = StringBuilder()
            var romanizedPronunciation = ""

            if (sentencesArray != null) {
                for (i in 0 until sentencesArray.length()) {
                    val sentenceItem = sentencesArray.optJSONArray(i) ?: continue
                    val transPart = sentenceItem.optString(0, "")
                    if (transPart.isNotBlank() && transPart != "null") {
                        translatedBuilder.append(transPart)
                    }

                    // Check for romanization/transliteration in the segment
                    if (sentenceItem.length() >= 3) {
                        val possibleRoman = sentenceItem.optString(2, "")
                        if (possibleRoman.isNotBlank() && possibleRoman != "null" && romanizedPronunciation.isBlank()) {
                            romanizedPronunciation = possibleRoman
                        }
                    }
                    if (sentenceItem.length() >= 4) {
                        val possibleRoman2 = sentenceItem.optString(3, "")
                        if (possibleRoman2.isNotBlank() && possibleRoman2 != "null" && romanizedPronunciation.isBlank()) {
                            romanizedPronunciation = possibleRoman2
                        }
                    }
                }
            }

            val finalTranslation = translatedBuilder.toString().trim()
            if (finalTranslation.isBlank()) {
                return@withContext Result.failure(Exception("Could not parse translation string"))
            }

            // If romanization is still blank, derive a friendly pronunciation representation
            val finalPronunciation = if (romanizedPronunciation.isNotBlank()) {
                romanizedPronunciation
            } else {
                finalTranslation
            }

            val etiquetteTip = getLanguageEtiquetteTip(targetLangCode, targetLangName)

            val result = TranslationResult(
                originalText = query,
                translatedText = finalTranslation,
                sourceLang = sourceLangName,
                targetLang = targetLangName,
                romanizedPronunciation = finalPronunciation,
                culturalEtiquetteTip = etiquetteTip,
                alternativePhrases = listOf(
                    "Polite / Formal: $finalTranslation",
                    "Casual / Conversational: $finalTranslation"
                )
            )

            Result.success(result)
        } catch (e: Exception) {
            Log.e(TAG, "Google Translate request exception", e)
            Result.failure(e)
        }
    }

    private fun getLanguageEtiquetteTip(targetLangCode: String, targetLangName: String): String {
        return when (targetLangCode.lowercase()) {
            "te" -> "In Andhra Pradesh & Telangana, speaking with 'andi' (అండి) at the end adds warmth and high respect."
            "hi" -> "Using 'Aap' (आप) and ending with 'ji' (जी) creates instant rapport and politeness across India."
            "kn" -> "In Karnataka, ending with 're' (ರೀ) or 'aagi' shows high courtesy to shopkeepers and elders."
            "ta" -> "In Tamil Nadu, a gentle nod with hands folded (Vanakkam) is deeply respected."
            "ml" -> "In Kerala, speaking in a calm, respectful tone with 'Chetta / Chechi' creates instant kinship."
            "or", "od" -> "In Odisha, saying 'Namaskar' with folded hands is customary before any greeting."
            "mr" -> "In Maharashtra, addressing locals as 'Dada' (brother) or 'Tai' (sister) is warm and polite."
            "bn" -> "In West Bengal, a gentle smile and addressing elders as 'Dada / Didi' is customary."
            "gu" -> "In Gujarat, 'Jai Shri Krishna' or 'Namaste' is a warm and welcoming gesture."
            "ja" -> "In Japan, a slight 15-degree bow when greeting or saying thank you is traditional etiquette."
            "fr" -> "In France, always say 'Bonjour' before asking a question or requesting service."
            "es" -> "In Spain and Latin America, greeting with 'Buenos días / Por favor' is essential."
            "de" -> "In Germany, clear and polite directness with 'Bitte / Danke' is appreciated."
            "th" -> "In Thailand, add 'khrap' (if male) or 'kha' (if female) at the end of sentences with a Wai gesture."
            "ar" -> "In the Middle East, placing your right hand over your chest when thanking is a sign of deep sincerity."
            else -> "Always smile and maintain respectful eye contact when conversing with local hosts."
        }
    }
}
