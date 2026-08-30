package com.example.data.model

data class FamousPlace(
    val id: String,
    val name: String,
    val localName: String? = null,
    val category: String, // Historical, Nature, Beach, Viewpoint, Temple, Market, Nightlife, Cafe, Adventure
    val description: String,
    val highlights: List<String>,
    val entryFeeInr: Double,
    val timings: String,
    val bestTimeToVisit: String,
    val insiderTip: String,
    val photoSpot: String? = null,
    val estimatedDurationHours: Double = 2.0,
    val trendingTag: String? = "🔥 Popular on Instagram & YouTube",
    val rating: Double = 4.8,
    val reviewCount: String = "10K+ online reviews",
    val areaOrNeighborhood: String? = null,
    val imageUrl: String? = null
)

data class LocalFoodItem(
    val id: String,
    val name: String,
    val regionalName: String? = null,
    val description: String,
    val dietType: String, // Vegetarian, Non-Veg, Vegan, Sweet/Dessert, Beverage
    val famousAtEatery: String,
    val averagePriceInr: Double,
    val mustTryReason: String,
    val flavorProfile: String, // Spicy, Tangy, Sweet, Crispy, Rich & Creamy
    val isStreetFood: Boolean = true,
    val trendingTag: String? = "👑 Viral Foodie Favorite",
    val rating: Double = 4.8,
    val areaOrNeighborhood: String? = null,
    val imageUrl: String? = null
)

data class DestinationExploreData(
    val destination: String,
    val stateOrRegion: String,
    val country: String,
    val tagline: String,
    val bestSeason: String,
    val safetyTip: String,
    val famousPlaces: List<FamousPlace>,
    val localFoods: List<LocalFoodItem>,
    val onlineTrendSummary: String = "Aggregated from YouTube Travel Vlogs, Instagram Reels, Google 4.5★+ reviews & TripAdvisor favorites.",
    val imageUrl: String? = null
)
