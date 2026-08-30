package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val destination: String,
    val startDate: Long,
    val endDate: Long,
    val budget: Double,
    val currencySymbol: String = "₹",
    val currencyCode: String = "INR",
    val tripType: String = "Vacation", // Vacation, Business, Road Trip, Backpacking, Solo, Family
    val colorHex: Long = 0xFF4F46E5,
    val groupMembers: String = "You, Alex, Priya, Rahul", // Comma-separated list of group members
    val createdAt: Long = System.currentTimeMillis()
) {
    val durationDays: Int
        get() {
            val diff = (endDate - startDate).coerceAtLeast(0)
            return ((diff / (1000 * 60 * 60 * 24)) + 1).toInt()
        }

    val membersList: List<String>
        get() = groupMembers.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .ifEmpty { listOf("You") }
}

