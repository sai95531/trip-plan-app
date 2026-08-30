package com.example.data.model

data class GeneratedItinerary(
    val tripTitle: String,
    val destination: String,
    val totalDays: Int,
    val leavesRequired: Int,
    val estimatedTotalBudgetInr: Double,
    val travelVibe: String,
    val bestSeasonToVisit: String,
    val transitAdvice: String,
    val packingEssentials: List<String>,
    val days: List<ItineraryDay>,
    val localInsiderTips: List<String>
)

data class ItineraryDay(
    val dayNumber: Int,
    val theme: String,
    val morningPlan: String,
    val afternoonPlan: String,
    val eveningPlan: String,
    val stayArea: String,
    val mustTryFood: String,
    val estimatedDailyExpenseInr: Double
)

data class LeaveTripOption(
    val leaveDays: Int,
    val totalDays: Int,
    val title: String,
    val description: String,
    val badge: String
)

enum class TravelVibe(val label: String, val emoji: String) {
    RELAXED("Relaxed & Leisure", "🌴"),
    ADVENTURE("Adventure & Treks", "🏔️"),
    CULTURAL("Culture & Heritage", "🏛️"),
    FOODIE("Food & Culinary", "🍲"),
    BUDGET_BACKPACKER("Budget Backpacker", "🎒"),
    LUXURY_WELLNESS("Luxury & Wellness", "✨")
}

enum class CompanionType(val label: String, val emoji: String) {
    SOLO("Solo Traveler", "🚶"),
    COUPLE("Couple / Romantic", "💑"),
    FRIENDS("Friends Group", "👥"),
    FAMILY("Family with Kids", "👨‍👩‍👧‍👦")
}
