package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gemini.GeminiClient
import com.example.data.model.SupportedLanguage
import com.example.data.model.TravelLanguages
import com.example.data.model.TranslationResult
import com.example.data.model.TravelerPhrase
import com.example.data.translation.GoogleTranslateEngine
import com.example.ui.translation.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TravelTranslatorViewModel(application: Application) : AndroidViewModel(application) {
    private val geminiClient = GeminiClient()
    private val ttsManager = TtsManager(application)

    val supportedLanguages = TravelLanguages.all

    val inputText = MutableStateFlow("")
    val sourceLanguage = MutableStateFlow(
        supportedLanguages.firstOrNull { it.code == "en" } ?: supportedLanguages[1]
    )
    val targetLanguage = MutableStateFlow(
        supportedLanguages.firstOrNull { it.code == "hi" } ?: supportedLanguages[0]
    )

    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()

    private val _latestResult = MutableStateFlow<TranslationResult?>(null)
    val latestResult: StateFlow<TranslationResult?> = _latestResult.asStateFlow()

    private val _translationHistory = MutableStateFlow<List<TranslationResult>>(emptyList())
    val translationHistory: StateFlow<List<TranslationResult>> = _translationHistory.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val quickConversations = listOf(
        "How much does this cost in Rupees?",
        "Please take me to the nearest railway station or airport.",
        "Is this vegetarian / without egg?",
        "Can you help me with directions to the beach?",
        "Please turn on the taxi meter.",
        "Where is the nearest clean washroom / restroom?",
        "Could I get drinking water / mineral water bottle?",
        "Can I pay using UPI / Google Pay / PhonePe QR code?",
        "Thank you very much for your help!",
        "Emergency: Please call a doctor or police!"
    )

    val curatedPhrasesByCategory = mapOf(
        "Shopping & Bargaining" to listOf(
            TravelerPhrase("p1", "Shopping", "How much is this?", "यह कितने का है?", "Yeh kitne ka hai?", "Point to item with a smile"),
            TravelerPhrase("p2", "Shopping", "Can you give a discount?", "कुछ कम कीजिए न?", "Kuch kam kijiye na?", "Friendly conversational bargaining"),
            TravelerPhrase("p3", "Shopping", "Can I pay with UPI / PhonePe?", "क्या UPI/PhonePe चलेगा?", "Kya UPI chalega?", "Point phone camera towards QR standee"),
            TravelerPhrase("p4", "Shopping", "I want to buy this.", "मुझे यह खरीदना है।", "Mujhe yeh khareedna hai.", "Clear decision indicator")
        ),
        "Food & Dining" to listOf(
            TravelerPhrase("p5", "Food", "Is this 100% vegetarian?", "क्या यह शुद्ध शाकाहारी है?", "Kya yeh shuddh shakahari hai?", "Standard dietary question"),
            TravelerPhrase("p6", "Food", "Please make it less spicy.", "कृपया कम तीखा बनाइए।", "Kripya kam teekha banaiye.", "Very useful for mild spice preferences"),
            TravelerPhrase("p7", "Food", "One bottle of drinking water please.", "एक पीने का पानी का बोतल दीजिए।", "Ek peene ka paani ka bottle dijiye.", "Check bottle seal before opening"),
            TravelerPhrase("p8", "Food", "Bill please.", "बिल दीजिए।", "Bill dijiye.", "Standard dining checkout")
        ),
        "Transport & Directions" to listOf(
            TravelerPhrase("p9", "Transport", "Where is the bus/auto stand?", "बस/ऑटो स्टैंड कहाँ है?", "Bus/Auto stand kahan hai?", "Ask nearby shopkeepers"),
            TravelerPhrase("p10", "Transport", "Please run by meter.", "कृपया मीटर से चलिए।", "Kripya meter se chaliye.", "Common request for auto drivers"),
            TravelerPhrase("p11", "Transport", "How far is the beach/hotel?", "बीच/होटल कितनी दूर है?", "Beach/Hotel kitni door hai?", "Distance inquiry")
        ),
        "Greetings & Politeness" to listOf(
            TravelerPhrase("p12", "Greetings", "Hello / Greetings", "नमस्ते", "Namaste", "Fold hands together with a gentle bow"),
            TravelerPhrase("p13", "Greetings", "Thank you very much", "बहुत-बहुत धन्यवाद", "Bahut bahut dhanyavaad", "Warm respectful gratitude"),
            TravelerPhrase("p14", "Greetings", "Excuse me / Sorry", "माफ़ कीजिए", "Maaf kijiye", "Polite attention or apology")
        )
    )

    fun swapLanguages() {
        val temp = sourceLanguage.value
        sourceLanguage.value = targetLanguage.value
        targetLanguage.value = temp
        if (latestResult.value != null) {
            inputText.value = latestResult.value!!.translatedText
        }
    }

    fun translate(textToTranslate: String = inputText.value) {
        val query = textToTranslate.trim()
        if (query.isBlank()) return

        viewModelScope.launch {
            _isTranslating.value = true
            _errorMessage.value = null

            // 1. First attempt Google Translate Engine (fast, highly accurate Google Translation endpoint)
            var tr: TranslationResult? = null
            try {
                val googleResult = GoogleTranslateEngine.translate(
                    text = query,
                    sourceLangCode = sourceLanguage.value.code,
                    targetLangCode = targetLanguage.value.code,
                    sourceLangName = sourceLanguage.value.name,
                    targetLangName = targetLanguage.value.name
                )
                googleResult.onSuccess { res ->
                    if (res.translatedText.isNotBlank() && res.translatedText != query) {
                        tr = res
                    }
                }
            } catch (e: Exception) {
                // Fallthrough to Gemini
            }

            // 2. If Google Translate returned empty or same text, try Gemini AI
            if (tr == null) {
                val geminiRes = geminiClient.translateTravelConversation(
                    text = query,
                    sourceLang = sourceLanguage.value.name,
                    targetLang = targetLanguage.value.name,
                    sourceLangCode = sourceLanguage.value.code,
                    targetLangCode = targetLanguage.value.code,
                    contextHint = "Travel conversation in ${targetLanguage.value.name}"
                )
                geminiRes.onSuccess {
                    tr = it
                }.onFailure {
                    // Fallback to Google Translate engine offline dictionary
                    val fallback = GoogleTranslateEngine.translate(
                        text = query,
                        sourceLangCode = sourceLanguage.value.code,
                        targetLangCode = targetLanguage.value.code,
                        sourceLangName = sourceLanguage.value.name,
                        targetLangName = targetLanguage.value.name
                    )
                    tr = fallback.getOrNull()
                }
            }

            _isTranslating.value = false
            if (tr != null) {
                _latestResult.value = tr
                _translationHistory.value = listOf(tr!!) + _translationHistory.value.take(19)
            } else {
                _errorMessage.value = "Translation failed. Please check network or try again."
            }
        }
    }

    fun speakText(text: String, languageCode: String = targetLanguage.value.code) {
        ttsManager.speak(text, languageCode)
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.release()
    }
}

