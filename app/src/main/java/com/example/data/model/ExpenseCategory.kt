package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class ExpenseCategory(
    val displayName: String,
    val colorHex: Long,
    val description: String
) {
    FOOD_DINING("Food & Dining", 0xFFF97316, "Restaurants, meals, food stalls"),
    LODGING("Lodging", 0xFF6366F1, "Hotels, hostels, resorts, Airbnb"),
    TRANSPORTATION("Transportation", 0xFF0EA5E9, "Flights, trains, taxis, metro, buses"),
    ACTIVITIES("Activities & Sights", 0xFF8B5CF6, "Museums, tours, entry tickets, theme parks"),
    SHOPPING("Shopping", 0xFFEC4899, "Souvenirs, clothing, duty-free, gifts"),
    CAFE_SNACKS("Cafe & Drinks", 0xFFD97706, "Coffee shops, bakeries, bars, nightlife"),
    FUEL_TOLLS("Fuel & Road Tolls", 0xFF059669, "Gasoline, highway tolls, parking"),
    ESSENTIALS("Health & Essentials", 0xFF10B981, "Pharmacies, SIM cards, toiletries, laundry"),
    OTHER("Other Expenses", 0xFF64748B, "Miscellaneous trip fees");

    val color: Color
        get() = Color(colorHex)

    val icon: ImageVector
        get() = when (this) {
            FOOD_DINING -> Icons.Default.Fastfood
            LODGING -> Icons.Default.Hotel
            TRANSPORTATION -> Icons.Default.Flight
            ACTIVITIES -> Icons.Default.TheaterComedy
            SHOPPING -> Icons.Default.ShoppingBag
            CAFE_SNACKS -> Icons.Default.LocalCafe
            FUEL_TOLLS -> Icons.Default.DirectionsCar
            ESSENTIALS -> Icons.Default.MedicalServices
            OTHER -> Icons.Default.MoreHoriz
        }

    companion object {
        fun fromString(name: String?): ExpenseCategory {
            if (name.isNullOrBlank()) return OTHER
            val cleaned = name.trim().lowercase()
            return entries.firstOrNull { 
                it.displayName.lowercase() == cleaned ||
                it.name.lowercase() == cleaned ||
                cleaned.contains(it.displayName.lowercase().take(5))
            } ?: when {
                cleaned.contains("food") || cleaned.contains("restaurant") || cleaned.contains("dinner") || cleaned.contains("lunch") || cleaned.contains("breakfast") || cleaned.contains("meal") -> FOOD_DINING
                cleaned.contains("hotel") || cleaned.contains("hostel") || cleaned.contains("lodging") || cleaned.contains("stay") || cleaned.contains("airbnb") -> LODGING
                cleaned.contains("flight") || cleaned.contains("train") || cleaned.contains("metro") || cleaned.contains("subway") || cleaned.contains("taxi") || cleaned.contains("uber") || cleaned.contains("grab") || cleaned.contains("transport") -> TRANSPORTATION
                cleaned.contains("ticket") || cleaned.contains("museum") || cleaned.contains("tour") || cleaned.contains("sight") || cleaned.contains("activity") || cleaned.contains("show") -> ACTIVITIES
                cleaned.contains("shop") || cleaned.contains("souvenir") || cleaned.contains("gift") || cleaned.contains("store") || cleaned.contains("clothes") -> SHOPPING
                cleaned.contains("cafe") || cleaned.contains("coffee") || cleaned.contains("starbucks") || cleaned.contains("bar") || cleaned.contains("beer") || cleaned.contains("snack") -> CAFE_SNACKS
                cleaned.contains("gas") || cleaned.contains("fuel") || cleaned.contains("toll") || cleaned.contains("parking") -> FUEL_TOLLS
                cleaned.contains("pharmacy") || cleaned.contains("health") || cleaned.contains("sim") || cleaned.contains("laundry") -> ESSENTIALS
                else -> OTHER
            }
        }
    }
}
