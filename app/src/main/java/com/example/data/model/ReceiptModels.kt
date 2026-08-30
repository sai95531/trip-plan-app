package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReceiptItem(
    val name: String,
    val quantity: Int = 1,
    val price: Double = 0.0
)

@JsonClass(generateAdapter = true)
data class ParsedReceipt(
    val merchant: String = "",
    val totalAmount: Double = 0.0,
    val currency: String = "USD",
    val category: String = "Food & Dining",
    val date: String = "",
    val paymentMethod: String = "Credit Card",
    val taxAmount: Double? = null,
    val tipAmount: Double? = null,
    val items: List<ReceiptItem> = emptyList(),
    val summaryNote: String = "",
    val tags: List<String> = emptyList()
)

data class AiSpendingInsight(
    val executiveSummary: String = "",
    val topSpendingPattern: String = "",
    val paceAndBudgetAssessment: String = "",
    val categoryHighlights: List<String> = emptyList(),
    val actionableSavingTips: List<String> = emptyList(),
    val projectedFinalSpend: Double? = null,
    val alertLevel: String = "NORMAL" // NORMAL, WARNING, DANGER
)
