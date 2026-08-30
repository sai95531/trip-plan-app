package com.example.data.gemini

import android.util.Log
import com.example.BuildConfig
import com.example.data.explore.DestinationCatalog
import com.example.data.model.AiSpendingInsight
import com.example.data.model.DestinationExploreData
import com.example.data.model.ExpenseEntity
import com.example.data.model.FamousPlace
import com.example.data.model.GeneratedItinerary
import com.example.data.model.ItineraryDay
import com.example.data.model.LocalFoodItem
import com.example.data.model.ParsedReceipt
import com.example.data.model.ReceiptItem
import com.example.data.model.TranslationResult
import com.example.data.model.TravelerPhrase
import com.example.data.model.TripEntity
import com.example.data.translation.OfflineTravelTranslator
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class GeminiClient {
    private val tag = "GeminiClient"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val service: GeminiApiService = retrofit.create(GeminiApiService::class.java)

    private fun getApiKey(): String {
        return BuildConfig.GEMINI_API_KEY
    }

    suspend fun planTripByLeaves(
        destination: String,
        leaveDays: Int,
        totalDays: Int,
        travelVibe: String,
        companionType: String,
        budgetInr: Double,
        startingCity: String,
        preferences: String,
        datesDescription: String = ""
    ): Result<GeneratedItinerary> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.success(
                    generateFallbackItinerary(
                        destination = destination.ifBlank { "Goa" },
                        leaveDays = leaveDays,
                        totalDays = totalDays,
                        travelVibe = travelVibe,
                        companionType = companionType,
                        budgetInr = budgetInr
                    )
                )
            }

            val prompt = buildString {
                appendLine("You are an expert travel planner specializing in Indian travelers and domestic/international tourism.")
                appendLine("Plan a comprehensive, realistic day-by-day travel itinerary optimized for someone with $leaveDays days of official leaves (making a total trip of $totalDays days including weekend/holidays).")
                if (datesDescription.isNotBlank()) {
                    appendLine("Selected Calendar Dates: $datesDescription")
                }
                appendLine("Destination: $destination")
                appendLine("Starting Origin City: ${startingCity.ifBlank { "Major Indian City" }}")
                appendLine("Travel Style / Vibe: $travelVibe")
                appendLine("Companion Group: $companionType")
                appendLine("Total Planned Budget: ₹$budgetInr INR")
                if (preferences.isNotBlank()) {
                    appendLine("User specific interests: $preferences")
                }
                appendLine("\nReturn ONLY valid JSON (no markdown wrappers, no backticks) with this exact schema:")
                appendLine("""
                {
                  "tripTitle": "Catchy Title for Trip (e.g. 4-Day Sun & Spice Goa Escape)",
                  "destination": "$destination",
                  "totalDays": $totalDays,
                  "leavesRequired": $leaveDays,
                  "estimatedTotalBudgetInr": $budgetInr,
                  "travelVibe": "$travelVibe",
                  "bestSeasonToVisit": "e.g. October to March",
                  "transitAdvice": "e.g. Flight to Goa Dabolim or Vande Bharat Train from Mumbai/Pune. Rent a self-drive scooty/car for local commuting.",
                  "packingEssentials": ["Sunscreen SPF 50", "Light cotton linen", "Flip flops", "Waterproof pouch"],
                  "days": [
                    {
                      "dayNumber": 1,
                      "theme": "Theme of day (e.g. Beach Shacks & Sunset Vibes)",
                      "morningPlan": "Morning activity details with time and specific spots",
                      "afternoonPlan": "Afternoon activity & lunch recommendation with famous local food",
                      "eveningPlan": "Evening sunset, cafe or night market spot",
                      "stayArea": "Recommended neighborhood to book stay (e.g. Candolim or Anjuna)",
                      "mustTryFood": "Specific dish (e.g. Goan Fish Curry Thali at Ritz Classic)",
                      "estimatedDailyExpenseInr": 3500.0
                    }
                  ],
                  "localInsiderTips": [
                    "Insider travel tip 1",
                    "Insider travel tip 2",
                    "Insider travel tip 3"
                  ]
                }
                """.trimIndent())
                appendLine("Ensure exactly $totalDays days are generated in the days array with realistic Indian Rupee (INR ₹) estimates.")
            }

            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.3f,
                    responseMimeType = "application/json"
                ),
                systemInstruction = GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = "You are a master itinerary designer who crafts practical, realistic travel plans for Indian travelers with INR budgeting."
                        )
                    )
                )
            )

            val response = service.generateContent(apiKey, request)
            val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
            if (responseText.isBlank()) {
                return@withContext Result.success(generateFallbackItinerary(destination, leaveDays, totalDays, travelVibe, companionType, budgetInr))
            }

            val cleanedJson = cleanJsonString(responseText)
            val json = JSONObject(cleanedJson)

            val daysList = mutableListOf<ItineraryDay>()
            val daysArray = json.optJSONArray("days")
            if (daysArray != null) {
                for (i in 0 until daysArray.length()) {
                    val dObj = daysArray.getJSONObject(i)
                    daysList.add(
                        ItineraryDay(
                            dayNumber = dObj.optInt("dayNumber", i + 1),
                            theme = dObj.optString("theme", "Day ${i + 1} Exploration"),
                            morningPlan = dObj.optString("morningPlan", "Explore local landmarks and scenic sights."),
                            afternoonPlan = dObj.optString("afternoonPlan", "Enjoy authentic local lunch and relaxed sightseeing."),
                            eveningPlan = dObj.optString("eveningPlan", "Sunset viewpoint and local evening market."),
                            stayArea = dObj.optString("stayArea", "Central Area"),
                            mustTryFood = dObj.optString("mustTryFood", "Local delicacy"),
                            estimatedDailyExpenseInr = dObj.optDouble("estimatedDailyExpenseInr", budgetInr / totalDays.coerceAtLeast(1))
                        )
                    )
                }
            }

            val packingList = mutableListOf<String>()
            val packArray = json.optJSONArray("packingEssentials")
            if (packArray != null) {
                for (i in 0 until packArray.length()) packingList.add(packArray.getString(i))
            }

            val tipsList = mutableListOf<String>()
            val tipsArray = json.optJSONArray("localInsiderTips")
            if (tipsArray != null) {
                for (i in 0 until tipsArray.length()) tipsList.add(tipsArray.getString(i))
            }

            val itinerary = GeneratedItinerary(
                tripTitle = json.optString("tripTitle", "$totalDays-Day $destination Trip"),
                destination = json.optString("destination", destination),
                totalDays = json.optInt("totalDays", totalDays),
                leavesRequired = json.optInt("leavesRequired", leaveDays),
                estimatedTotalBudgetInr = json.optDouble("estimatedTotalBudgetInr", budgetInr),
                travelVibe = json.optString("travelVibe", travelVibe),
                bestSeasonToVisit = json.optString("bestSeasonToVisit", "Best visited during pleasant weather months"),
                transitAdvice = json.optString("transitAdvice", "Use local cabs or train for transit."),
                packingEssentials = packingList.ifEmpty { listOf("Comfortable walking shoes", "Sunscreen & Sunglasses", "Power bank", "Valid ID Cards") },
                days = daysList.ifEmpty {
                    generateFallbackItinerary(destination, leaveDays, totalDays, travelVibe, companionType, budgetInr).days
                },
                localInsiderTips = tipsList.ifEmpty {
                    listOf("Book flights & trains in advance for best INR fares.", "Try street food from busy, high-turnover stalls.", "Carry cash for local auto-rickshaws and street vendors.")
                }
            )

            Result.success(itinerary)
        } catch (e: Exception) {
            Log.e(tag, "Error generating itinerary with Gemini", e)
            Result.success(generateFallbackItinerary(destination, leaveDays, totalDays, travelVibe, companionType, budgetInr))
        }
    }

    suspend fun getFamousPlacesAndFoods(
        destination: String,
        locationHint: String = ""
    ): Result<DestinationExploreData> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            val targetDest = destination.ifBlank { locationHint.ifBlank { "Bangalore" } }

            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.success(getCuratedDestinationData(targetDest))
            }

            val prompt = buildString {
                appendLine("You are an elite multi-source travel intelligence researcher and cultural culinary expert.")
                appendLine("Synthesize and aggregate comprehensive, up-to-date travel recommendations for: $targetDest by cross-referencing information across multiple leading web sources:")
                appendLine("1. YouTube Travel Vlogs & Creator Guides (drone spots, walking tours, hidden trails, street food crawls)")
                appendLine("2. Instagram & TikTok Visual Hotspots (viral reels, golden hour photography angles, aesthetic rooftop cafes & secret photo spots)")
                appendLine("3. Google Maps Local Guides (4.5★+ top ratings, realistic opening hours, recent entry tickets in INR ₹, crowd avoidance times)")
                appendLine("4. TripAdvisor Travelers' Choice & Reddit City Forums (r/travel, r/india, r/$targetDest recommendations and verified local tips)")
                appendLine("5. Culinary Heritage Archives & Local Food Blogs (iconic 50+ year old heritage stalls, regional breakfast legends, sweet shops, signature dishes)")
                appendLine("DO NOT LIMIT TO ONLY A FEW PLACES. Provide a rich, diverse catalog of AT LEAST 15 TO 20 MUST-VISIT ATTRACTIONS and AT LEAST 10 TO 15 AUTHENTIC LOCAL FOOD SPECIALTIES.")
                appendLine("Cover a wide spectrum: Heritage & Palaces, Nature & Botanical Parks, Instagrammable / Aesthetic Spots, Viewpoints & Sunsets, Spiritual & Ancient Temples, Cafes & Craft Breweries, Vibrant Bazaars & Night Markets, and Adventure Outings.")
                appendLine("All currency prices MUST be in Indian Rupees (₹ INR).")
                appendLine("Return ONLY valid JSON (no markdown backticks, no wrapping) matching this schema:")
                appendLine("""
                {
                  "destination": "$targetDest",
                  "stateOrRegion": "State or Region name",
                  "country": "India or Country",
                  "tagline": "Captivating 1-line description highlighting what makes it special",
                  "bestSeason": "e.g. October to March / Pleasant all year",
                  "safetyTip": "Practical local safety & transit advice for travelers",
                  "onlineTrendSummary": "Aggregated from trending YouTube travel vlogs, Instagram Reels, Google 4.6★+ reviews & Reddit local favorites.",
                  "famousPlaces": [
                    {
                      "id": "place_1",
                      "name": "Attraction Name",
                      "localName": "Local Language Name or Moniker",
                      "category": "Historical, Nature, Viewpoint, Instagrammable, Temple, Market, Rooftop / Cafe, Nightlife, Cultural, Adventure",
                      "description": "2-3 sentences explaining why travelers love it and what to experience",
                      "highlights": ["Highlight 1", "Highlight 2", "Highlight 3"],
                      "entryFeeInr": 100.0,
                      "timings": "6:00 AM - 7:00 PM",
                      "bestTimeToVisit": "Morning 7 AM / Golden hour sunset",
                      "insiderTip": "Insider secret or avoid crowd tip",
                      "photoSpot": "Best angle or reel location for photos",
                      "estimatedDurationHours": 2.5,
                      "trendingTag": "🔥 Instagram Viral Reel Spot (or 📹 YouTube Vlog Top Pick / ⭐ Google 4.8★ Top Rated / ✨ Hidden Gem / ☕ Aesthetic Cafe)",
                      "rating": 4.8,
                      "reviewCount": "25K+ online reviews",
                      "areaOrNeighborhood": "Specific neighborhood/area name"
                    }
                  ],
                  "localFoods": [
                    {
                      "id": "food_1",
                      "name": "Food Dish Name",
                      "regionalName": "Local name in regional language/script",
                      "description": "Mouthwatering description of the dish and preparation",
                      "dietType": "Vegetarian, Non-Veg, Sweet/Dessert, or Beverage",
                      "famousAtEatery": "Exact legendary restaurant, iconic cafe or street stall name",
                      "averagePriceInr": 150.0,
                      "mustTryReason": "Why this is an unmissable culinary heritage dish",
                      "flavorProfile": "Crispy, Ghee-roasted, Spicy, Tangy, Coconutty, Sweet",
                      "isStreetFood": true,
                      "trendingTag": "👑 Viral Foodie Reel Favorite / 80-Yr Heritage Stall",
                      "rating": 4.9,
                      "areaOrNeighborhood": "Neighborhood or street name"
                    }
                  ]
                }
                """.trimIndent())
                appendLine("Ensure there are at least 15 unique places and at least 10 unique food items in the JSON array.")
            }

            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.25f,
                    responseMimeType = "application/json"
                ),
                systemInstruction = GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = "You are a comprehensive travel data engine synthesizing online travel blogs, social media reels, and local culinary discoveries."
                        )
                    )
                )
            )

            val response = service.generateContent(apiKey, request)
            val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
            if (responseText.isBlank()) {
                return@withContext Result.success(getCuratedDestinationData(targetDest))
            }

            val cleaned = cleanJsonString(responseText)
            val json = JSONObject(cleaned)

            val places = mutableListOf<FamousPlace>()
            val placesArray = json.optJSONArray("famousPlaces")
            if (placesArray != null) {
                for (i in 0 until placesArray.length()) {
                    val p = placesArray.getJSONObject(i)
                    val hlList = mutableListOf<String>()
                    val hlArr = p.optJSONArray("highlights")
                    if (hlArr != null) {
                        for (k in 0 until hlArr.length()) hlList.add(hlArr.getString(k))
                    }
                    places.add(
                        FamousPlace(
                            id = p.optString("id", "place_$i"),
                            name = p.optString("name", "Famous Landmark"),
                            localName = p.optString("localName").takeIf { it.isNotBlank() },
                            category = p.optString("category", "Sightseeing"),
                            description = p.optString("description", "A must-visit scenic tourist landmark."),
                            highlights = hlList.ifEmpty { listOf("Iconic architectural beauty", "Scenic views") },
                            entryFeeInr = p.optDouble("entryFeeInr", 0.0),
                            timings = p.optString("timings", "Open Daily"),
                            bestTimeToVisit = p.optString("bestTimeToVisit", "Morning or Evening"),
                            insiderTip = p.optString("insiderTip", "Arrive early to avoid queues."),
                            photoSpot = p.optString("photoSpot", "Main viewpoint"),
                            estimatedDurationHours = p.optDouble("estimatedDurationHours", 2.0),
                            trendingTag = p.optString("trendingTag", "🔥 Trending on Social Media").takeIf { it.isNotBlank() } ?: "🔥 Trending on Social Media",
                            rating = p.optDouble("rating", 4.8).coerceIn(4.0, 5.0),
                            reviewCount = p.optString("reviewCount", "10K+ online reviews"),
                            areaOrNeighborhood = p.optString("areaOrNeighborhood").takeIf { it.isNotBlank() }
                        )
                    )
                }
            }

            val foods = mutableListOf<LocalFoodItem>()
            val foodsArray = json.optJSONArray("localFoods")
            if (foodsArray != null) {
                for (i in 0 until foodsArray.length()) {
                    val f = foodsArray.getJSONObject(i)
                    foods.add(
                        LocalFoodItem(
                            id = f.optString("id", "food_$i"),
                            name = f.optString("name", "Local Specialty"),
                            regionalName = f.optString("regionalName").takeIf { it.isNotBlank() },
                            description = f.optString("description", "Famous regional delicacy made with traditional spices."),
                            dietType = f.optString("dietType", "Vegetarian"),
                            famousAtEatery = f.optString("famousAtEatery", "Local street stalls & iconic eateries"),
                            averagePriceInr = f.optDouble("averagePriceInr", 150.0),
                            mustTryReason = f.optString("mustTryReason", "Authentic heritage taste."),
                            flavorProfile = f.optString("flavorProfile", "Spicy & Aromatic"),
                            isStreetFood = f.optBoolean("isStreetFood", true),
                            trendingTag = f.optString("trendingTag", "👑 Viral Foodie Favorite").takeIf { it.isNotBlank() } ?: "👑 Viral Foodie Favorite",
                            rating = f.optDouble("rating", 4.8).coerceIn(4.0, 5.0),
                            areaOrNeighborhood = f.optString("areaOrNeighborhood").takeIf { it.isNotBlank() }
                        )
                    )
                }
            }

            val result = DestinationExploreData(
                destination = json.optString("destination", targetDest),
                stateOrRegion = json.optString("stateOrRegion", "India"),
                country = json.optString("country", "India"),
                tagline = json.optString("tagline", "Explore the top sights and authentic culinary wonders of $targetDest"),
                bestSeason = json.optString("bestSeason", "October to March"),
                safetyTip = json.optString("safetyTip", "Keep your belongings safe in crowded bazaars and use ride-hailing apps (Uber/Ola/Namma Yatri) for transparent fares."),
                famousPlaces = places.ifEmpty { getCuratedDestinationData(targetDest).famousPlaces },
                localFoods = foods.ifEmpty { getCuratedDestinationData(targetDest).localFoods },
                onlineTrendSummary = json.optString("onlineTrendSummary", "Aggregated from YouTube Travel Vlogs, Instagram Reels, Google 4.5★+ reviews & TripAdvisor favorites.")
            )
            Result.success(result)
        } catch (e: Exception) {
            Log.e(tag, "Error fetching places & foods with Gemini", e)
            Result.success(getCuratedDestinationData(destination.ifBlank { "Bangalore" }))
        }
    }

    suspend fun translateTravelConversation(
        text: String,
        sourceLang: String,
        targetLang: String,
        sourceLangCode: String = "en",
        targetLangCode: String = "hi",
        contextHint: String = ""
    ): Result<TranslationResult> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.success(
                    OfflineTravelTranslator.translateOffline(
                        text = text,
                        sourceLangCode = sourceLangCode,
                        targetLangCode = targetLangCode,
                        sourceLangName = sourceLang,
                        targetLangName = targetLang
                    )
                )
            }

            val prompt = buildString {
                appendLine("You are an expert real-time travel translator and speech assistant.")
                appendLine("Translate the following phrase accurately for a natural, polite conversation between a tourist and a local resident.")
                appendLine("Source Language: $sourceLang ($sourceLangCode)")
                appendLine("Target Language: $targetLang ($targetLangCode)")
                appendLine("Context / Tone: ${contextHint.ifBlank { "Friendly travel conversation (shopping, ordering food, directions, taxi, emergency)" }}")
                appendLine("Text to translate: \"$text\"")
                appendLine("\nReturn ONLY valid JSON (no markdown wrappers, no backticks) with this structure:")
                appendLine("""
                {
                  "originalText": "$text",
                  "translatedText": "Accurate natural translation in the target language script",
                  "sourceLang": "$sourceLang",
                  "targetLang": "$targetLang",
                  "romanizedPronunciation": "Easy-to-read English phonetics/transliteration for how to pronounce it correctly",
                  "culturalEtiquetteTip": "1-sentence tip on politeness or cultural body language / gesture when saying this",
                  "alternativePhrases": ["Shorter polite alternative", "Colloquial local variant"]
                }
                """.trimIndent())
            }

            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.1f,
                    responseMimeType = "application/json"
                ),
                systemInstruction = GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = "You are a professional travel linguist facilitating smooth cross-cultural conversations with phonetic pronunciation guides."
                        )
                    )
                )
            )

            val response = service.generateContent(apiKey, request)
            val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
            if (responseText.isBlank()) {
                return@withContext Result.success(
                    OfflineTravelTranslator.translateOffline(
                        text = text,
                        sourceLangCode = sourceLangCode,
                        targetLangCode = targetLangCode,
                        sourceLangName = sourceLang,
                        targetLangName = targetLang
                    )
                )
            }

            val cleaned = cleanJsonString(responseText)
            val json = JSONObject(cleaned)

            val altList = mutableListOf<String>()
            val altArr = json.optJSONArray("alternativePhrases")
            if (altArr != null) {
                for (i in 0 until altArr.length()) altList.add(altArr.getString(i))
            }

            val transText = json.optString("translatedText", "").ifBlank {
                OfflineTravelTranslator.translateOffline(text, sourceLangCode, targetLangCode, sourceLang, targetLang).translatedText
            }

            val result = TranslationResult(
                originalText = json.optString("originalText", text),
                translatedText = transText,
                sourceLang = json.optString("sourceLang", sourceLang),
                targetLang = json.optString("targetLang", targetLang),
                romanizedPronunciation = json.optString("romanizedPronunciation", transText),
                culturalEtiquetteTip = json.optString("culturalEtiquetteTip", "Be polite and friendly with locals."),
                alternativePhrases = altList
            )
            Result.success(result)
        } catch (e: Exception) {
            Log.e(tag, "Error in translation with Gemini, using offline dictionary fallback", e)
            Result.success(
                OfflineTravelTranslator.translateOffline(
                    text = text,
                    sourceLangCode = sourceLangCode,
                    targetLangCode = targetLangCode,
                    sourceLangName = sourceLang,
                    targetLangName = targetLang
                )
            )
        }
    }

    suspend fun parseReceipt(
        imageBase64: String? = null,
        receiptText: String? = null,
        userHint: String? = null
    ): Result<ParsedReceipt> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                Log.w(tag, "Gemini API key is placeholder. Attempting mock fallback or warning.")
                return@withContext Result.failure(
                    IllegalStateException("Gemini API key is not configured in Secrets panel.")
                )
            }

            val parts = mutableListOf<GeminiPart>()

            val promptText = buildString {
                appendLine("You are an expert AI receipt parser and trip expense categorizer.")
                appendLine("Analyze the provided receipt (image or text) and return ONLY a valid raw JSON object (NO markdown wrappers, no backticks, no extra text) with the following structure:")
                appendLine("""
                {
                  "merchant": "Merchant or Store Name",
                  "totalAmount": 1234.50,
                  "currency": "INR" (or USD, EUR, JPY, GBP, etc),
                  "category": "One of: Food & Dining, Lodging, Transportation, Activities & Sights, Shopping, Cafe & Drinks, Fuel & Road Tolls, Health & Essentials, Other Expenses",
                  "date": "YYYY-MM-DD",
                  "paymentMethod": "UPI / GPay / PhonePe, Credit Card, Cash, or Net Banking",
                  "taxAmount": 0.0,
                  "tipAmount": 0.0,
                  "items": [
                    {"name": "Item description", "quantity": 1, "price": 250.0}
                  ],
                  "summaryNote": "A helpful 1-sentence description of the purchase",
                  "tags": ["Dinner", "Goa", "Seafood"]
                }
                """.trimIndent())
                if (!receiptText.isNullOrBlank()) {
                    appendLine("Receipt Text/Details:")
                    appendLine(receiptText)
                }
                if (!userHint.isNullOrBlank()) {
                    appendLine("User note/context: $userHint")
                }
                appendLine("Carefully extract total amount in Indian Rupees (₹ INR) or relevant currency and automatically select the most accurate Category.")
            }

            parts.add(GeminiPart(text = promptText))

            if (!imageBase64.isNullOrBlank()) {
                parts.add(
                    GeminiPart(
                        inlineData = GeminiInlineData(
                            mimeType = "image/jpeg",
                            data = imageBase64
                        )
                    )
                )
            }

            val request = GeminiRequest(
                contents = listOf(GeminiContent(parts = parts)),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.1f,
                    responseMimeType = "application/json"
                ),
                systemInstruction = GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = "You are a specialized financial receipt scanner that extracts structured JSON data with extreme accuracy."
                        )
                    )
                )
            )

            val response = service.generateContent(apiKey, request)
            val responseText = response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text ?: ""

            if (responseText.isBlank()) {
                return@withContext Result.failure(Exception("Gemini returned an empty response."))
            }

            val cleanedJson = cleanJsonString(responseText)
            val parsed = parseReceiptJson(cleanedJson)
            Result.success(parsed)
        } catch (e: Exception) {
            Log.e(tag, "Error parsing receipt with Gemini", e)
            Result.failure(e)
        }
    }

    suspend fun summarizeSpendingPatterns(
        trip: TripEntity,
        expenses: List<ExpenseEntity>
    ): Result<AiSpendingInsight> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext Result.failure(
                    IllegalStateException("Gemini API key is not configured in Secrets panel.")
                )
            }

            if (expenses.isEmpty()) {
                return@withContext Result.success(
                    AiSpendingInsight(
                        executiveSummary = "No expenses recorded yet for ${trip.name}. Start adding or scanning receipts to get intelligent spending insights in ${trip.currencySymbol} INR!",
                        topSpendingPattern = "No transactions found.",
                        paceAndBudgetAssessment = "Budget is 100% available (${trip.currencySymbol}${String.format(Locale.US, "%.2f", trip.budget)}).",
                        categoryHighlights = listOf("Log your first expense to see automatic category breakdown."),
                        actionableSavingTips = listOf("Scan UPI payment screenshots and bills as you travel."),
                        projectedFinalSpend = 0.0,
                        alertLevel = "NORMAL"
                    )
                )
            }

            val totalSpent = expenses.sumOf { it.amount }
            val remainingBudget = trip.budget - totalSpent
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateStr = dateFormat.format(Date(trip.startDate))
            val endDateStr = dateFormat.format(Date(trip.endDate))

            val ledgerSummary = buildString {
                appendLine("Trip: ${trip.name} (${trip.destination})")
                appendLine("Dates: $startDateStr to $endDateStr (${trip.durationDays} days)")
                appendLine("Trip Type: ${trip.tripType}")
                appendLine("Total Budget: ${trip.currencySymbol}${trip.budget} (${trip.currencyCode})")
                appendLine("Total Spent So Far: ${trip.currencySymbol}${totalSpent} (${expenses.size} expenses)")
                appendLine("Remaining Budget: ${trip.currencySymbol}${remainingBudget}")
                appendLine("\nExpense Log:")
                expenses.forEachIndexed { index, exp ->
                    val expDate = dateFormat.format(Date(exp.date))
                    appendLine("${index + 1}. [$expDate] ${exp.title} - ${trip.currencySymbol}${exp.amount} | Category: ${exp.category} | Method: ${exp.paymentMethod}${if (exp.notes.isNotBlank()) " | Note: ${exp.notes}" else ""}")
                }
            }

            val prompt = buildString {
                appendLine("Analyze the following trip expenses and generate a comprehensive spending pattern summary.")
                appendLine("Return ONLY valid JSON (no backticks, no code blocks) matching this schema:")
                appendLine("""
                {
                  "executiveSummary": "Concise 2-3 sentence overview evaluating spending health and trajectory",
                  "topSpendingPattern": "Detailed analysis of where money is going (highest categories, peak spending days, splurge vs essential ratio)",
                  "paceAndBudgetAssessment": "Analysis of daily burn rate versus planned daily budget and risk of overrun",
                  "categoryHighlights": [
                    "Highlight 1 (e.g. Dining accounts for 45% of total budget)",
                    "Highlight 2 (e.g. Transportation costs are well controlled)",
                    "Highlight 3"
                  ],
                  "actionableSavingTips": [
                    "Specific actionable tip 1 for the remaining days",
                    "Specific actionable tip 2 tailored to this destination and spending behavior",
                    "Specific actionable tip 3"
                  ],
                  "projectedFinalSpend": 1234.50,
                  "alertLevel": "NORMAL" (or "WARNING" if spending fast, "DANGER" if over budget)
                }
                """.trimIndent())
                appendLine("\nTrip Data:\n$ledgerSummary")
            }

            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt))
                    )
                ),
                generationConfig = GeminiGenerationConfig(
                    temperature = 0.2f,
                    responseMimeType = "application/json"
                ),
                systemInstruction = GeminiContent(
                    parts = listOf(
                        GeminiPart(
                            text = "You are a senior travel financial advisor and spending analyst who provides deep, actionable spending pattern summaries."
                        )
                    )
                )
            )

            val response = service.generateContent(apiKey, request)
            val responseText = response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text ?: ""

            if (responseText.isBlank()) {
                return@withContext Result.failure(Exception("Empty response from Gemini"))
            }

            val cleanedJson = cleanJsonString(responseText)
            val json = JSONObject(cleanedJson)

            val categoryHighlightsList = mutableListOf<String>()
            val catArray = json.optJSONArray("categoryHighlights")
            if (catArray != null) {
                for (i in 0 until catArray.length()) {
                    categoryHighlightsList.add(catArray.getString(i))
                }
            }

            val savingTipsList = mutableListOf<String>()
            val tipsArray = json.optJSONArray("actionableSavingTips")
            if (tipsArray != null) {
                for (i in 0 until tipsArray.length()) {
                    savingTipsList.add(tipsArray.getString(i))
                }
            }

            val insight = AiSpendingInsight(
                executiveSummary = json.optString("executiveSummary", "Spending pattern analysis complete."),
                topSpendingPattern = json.optString("topSpendingPattern", "Analysis shows diverse trip spending across categories."),
                paceAndBudgetAssessment = json.optString("paceAndBudgetAssessment", "On track with planned budget."),
                categoryHighlights = categoryHighlightsList.ifEmpty {
                    listOf("Food and Lodging are the primary expense drivers.")
                },
                actionableSavingTips = savingTipsList.ifEmpty {
                    listOf("Monitor daily dining and incidental purchases.")
                },
                projectedFinalSpend = json.optDouble("projectedFinalSpend", totalSpent).takeIf { !it.isNaN() },
                alertLevel = json.optString("alertLevel", if (remainingBudget < 0) "DANGER" else "NORMAL")
            )

            Result.success(insight)
        } catch (e: Exception) {
            Log.e(tag, "Error summarizing spending patterns", e)
            Result.failure(e)
        }
    }

    private fun cleanJsonString(raw: String): String {
        var trimmed = raw.trim()
        if (trimmed.startsWith("```json")) {
            trimmed = trimmed.removePrefix("```json")
        } else if (trimmed.startsWith("```")) {
            trimmed = trimmed.removePrefix("```")
        }
        if (trimmed.endsWith("```")) {
            trimmed = trimmed.removeSuffix("```")
        }
        return trimmed.trim()
    }

    private fun parseReceiptJson(jsonStr: String): ParsedReceipt {
        val json = JSONObject(jsonStr)
        val itemsList = mutableListOf<ReceiptItem>()
        val itemsArray = json.optJSONArray("items")
        if (itemsArray != null) {
            for (i in 0 until itemsArray.length()) {
                val itemObj = itemsArray.getJSONObject(i)
                itemsList.add(
                    ReceiptItem(
                        name = itemObj.optString("name", "Item ${i + 1}"),
                        quantity = itemObj.optInt("quantity", 1),
                        price = itemObj.optDouble("price", 0.0)
                    )
                )
            }
        }

        val tagsList = mutableListOf<String>()
        val tagsArray = json.optJSONArray("tags")
        if (tagsArray != null) {
            for (i in 0 until tagsArray.length()) {
                tagsList.add(tagsArray.getString(i))
            }
        }

        return ParsedReceipt(
            merchant = json.optString("merchant", "Merchant"),
            totalAmount = json.optDouble("totalAmount", 0.0),
            currency = json.optString("currency", "INR"),
            category = json.optString("category", "Food & Dining"),
            date = json.optString("date", ""),
            paymentMethod = json.optString("paymentMethod", "UPI"),
            taxAmount = if (json.has("taxAmount")) json.optDouble("taxAmount") else null,
            tipAmount = if (json.has("tipAmount")) json.optDouble("tipAmount") else null,
            items = itemsList,
            summaryNote = json.optString("summaryNote", ""),
            tags = tagsList
        )
    }

    private fun generateFallbackItinerary(
        destination: String,
        leaveDays: Int,
        totalDays: Int,
        travelVibe: String,
        companionType: String,
        budgetInr: Double
    ): GeneratedItinerary {
        val daysList = (1..totalDays).map { dayNum ->
            val dayThemes = when (dayNum) {
                1 -> "Arrival, Check-in & Iconic Sunset Vibe"
                2 -> "Heritage Sights, Forts & Local Gastronomy"
                3 -> "Adventure, Water Activities & Beach Hopping"
                4 -> "Local Bazaars, Handicrafts & Spice Plantation"
                5 -> "Hidden Waterfalls & Scenic Scenic Viewpoints"
                6 -> "Relaxing Spa, Sunset Cruise & Fine Dining"
                else -> "Leisure Morning & Souvenir Shopping"
            }
            ItineraryDay(
                dayNumber = dayNum,
                theme = dayThemes,
                morningPlan = "Morning visit to top landmark in $destination, capture sunrise photos, and savor regional breakfast.",
                afternoonPlan = "Authentic regional lunch at iconic local restaurant, followed by heritage sight exploration.",
                eveningPlan = "Sunset view at top rated viewpoint, followed by lively night market and street food trail.",
                stayArea = "Central $destination Promenade / Heritage Quarter",
                mustTryFood = "Famous regional specialty thali & dessert",
                estimatedDailyExpenseInr = (budgetInr / totalDays.coerceAtLeast(1)).coerceAtLeast(1500.0)
            )
        }

        return GeneratedItinerary(
            tripTitle = "$totalDays-Day $destination $travelVibe Getaway",
            destination = destination,
            totalDays = totalDays,
            leavesRequired = leaveDays,
            estimatedTotalBudgetInr = budgetInr,
            travelVibe = travelVibe,
            bestSeasonToVisit = "October to March (Pleasant weather)",
            transitAdvice = "Fly into the nearest airport or take express train. Rent a self-drive scooter/cab for seamless intercity travel.",
            packingEssentials = listOf("Breathable cotton clothes", "Sunscreen SPF 50 & shades", "Power bank", "Cash for street vendors", "Comfortable walking footwear"),
            days = daysList,
            localInsiderTips = listOf(
                "Use UPI (Google Pay / PhonePe) which is accepted almost everywhere in India.",
                "Hire registered guides at monuments for authentic history stories.",
                "Negotiate auto-rickshaw fares or ask them to use the meter."
            )
        )
    }

    private fun getCuratedDestinationData(destination: String): DestinationExploreData {
        return DestinationCatalog.getDestination(destination)
    }
}
