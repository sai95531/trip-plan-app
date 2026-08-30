package com.example.data.model

data class TranslationResult(
    val originalText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val romanizedPronunciation: String,
    val culturalEtiquetteTip: String? = null,
    val alternativePhrases: List<String> = emptyList()
)

data class TravelerPhrase(
    val id: String,
    val category: String, // Bargaining, Food & Dining, Directions & Taxi, Greetings, Emergency
    val englishText: String,
    val translatedText: String,
    val pronunciation: String,
    val explanation: String
)

data class LiveChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val senderIsUser: Boolean,
    val originalText: String,
    val translatedText: String,
    val pronunciation: String,
    val languageCode: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SupportedLanguage(
    val code: String,
    val name: String,
    val nativeName: String,
    val flagEmoji: String,
    val ttsLocaleTag: String
)

object TravelLanguages {
    val all = listOf(
        SupportedLanguage("hi", "Hindi", "हिन्दी", "🇮🇳", "hi-IN"),
        SupportedLanguage("en", "English", "English", "🌐", "en-US"),
        SupportedLanguage("ta", "Tamil", "தமிழ்", "🇮🇳", "ta-IN"),
        SupportedLanguage("te", "Telugu", "తెలుగు", "🇮🇳", "te-IN"),
        SupportedLanguage("kn", "Kannada", "ಕನ್ನಡ", "🇮🇳", "kn-IN"),
        SupportedLanguage("ml", "Malayalam", "മലയാളം", "🇮🇳", "ml-IN"),
        SupportedLanguage("bn", "Bengali", "বাংলা", "🇮🇳", "bn-IN"),
        SupportedLanguage("mr", "Marathi", "मराठी", "🇮🇳", "mr-IN"),
        SupportedLanguage("gu", "Gujarati", "ગુજરાતી", "🇮🇳", "gu-IN"),
        SupportedLanguage("pa", "Punjabi", "ਪੰਜਾਬੀ", "🇮🇳", "pa-IN"),
        SupportedLanguage("ja", "Japanese", "日本語", "🇯🇵", "ja-JP"),
        SupportedLanguage("fr", "French", "Français", "🇫🇷", "fr-FR"),
        SupportedLanguage("es", "Spanish", "Español", "🇪🇸", "es-ES"),
        SupportedLanguage("th", "Thai", "ไทย", "🇹🇭", "th-TH"),
        SupportedLanguage("de", "German", "Deutsch", "🇩🇪", "de-DE"),
        SupportedLanguage("it", "Italian", "Italiano", "🇮🇹", "it-IT"),
        SupportedLanguage("ar", "Arabic", "العربية", "🇦🇪", "ar-SA")
    )
}
