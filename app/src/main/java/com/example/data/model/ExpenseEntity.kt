package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["tripId"])]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tripId: Long,
    val title: String,
    val amount: Double,
    val category: String, // String representation of ExpenseCategory
    val date: Long = System.currentTimeMillis(),
    val paymentMethod: String = "Credit Card", // Credit Card, Cash, Debit Card, Mobile Pay, Other
    val notes: String = "",
    val isAiParsed: Boolean = false,
    val tags: String = "", // Comma separated
    val itemsJson: String = "", // JSON list of itemized ReceiptItem
    val receiptImageBase64: String? = null,
    val location: String = "",
    val paidBy: String = "You", // Name of member who paid (e.g. You, Alex)
    val splitType: String = "EQUAL", // EQUAL, YOU_ONLY, CUSTOM
    val splitWith: String = "", // Comma-separated list of members sharing this, empty = all trip members
    val customSplitsJson: String = "" // Optional custom amount breakdown
) {
    val expenseCategory: ExpenseCategory
        get() = ExpenseCategory.fromString(category)

    fun getSplitMembers(allTripMembers: List<String>): List<String> {
        if (splitType == "YOU_ONLY") {
            return listOf(paidBy.ifBlank { "You" })
        }
        val customMembers = splitWith.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
        return if (customMembers.isNotEmpty()) customMembers else allTripMembers.ifEmpty { listOf("You") }
    }
}

