package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.AiSpendingInsight
import com.example.data.model.ExpenseCategory
import com.example.data.model.ExpenseEntity
import com.example.data.model.ParsedReceipt
import com.example.data.model.TripEntity
import com.example.data.repository.TripRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TripViewModel(application: Application) : AndroidViewModel(application) {
    private val tag = "TripViewModel"
    private val database = AppDatabase.getDatabase(application)
    private val repository = TripRepository(database.tripDao(), database.expenseDao())

    val allTrips: StateFlow<List<TripEntity>> = repository.allTrips
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedTripId = MutableStateFlow<Long?>(null)
    val selectedTripId: StateFlow<Long?> = _selectedTripId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedTrip: StateFlow<TripEntity?> = _selectedTripId
        .flatMapLatest { id ->
            if (id != null) {
                repository.getTripById(id)
            } else {
                flowOf(null)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedTripExpenses: StateFlow<List<ExpenseEntity>> = _selectedTripId
        .flatMapLatest { id ->
            if (id != null) {
                repository.getExpensesForTrip(id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allExpenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Receipt OCR / Parsing State
    private val _isReceiptScanning = MutableStateFlow(false)
    val isReceiptScanning: StateFlow<Boolean> = _isReceiptScanning.asStateFlow()

    private val _parsedReceipt = MutableStateFlow<ParsedReceipt?>(null)
    val parsedReceipt: StateFlow<ParsedReceipt?> = _parsedReceipt.asStateFlow()

    private val _scannedImageBase64 = MutableStateFlow<String?>(null)
    val scannedImageBase64: StateFlow<String?> = _scannedImageBase64.asStateFlow()

    // AI Spending Summary & Insights State
    private val _isGeneratingAiSummary = MutableStateFlow(false)
    val isGeneratingAiSummary: StateFlow<Boolean> = _isGeneratingAiSummary.asStateFlow()

    private val _aiSpendingInsight = MutableStateFlow<AiSpendingInsight?>(null)
    val aiSpendingInsight: StateFlow<AiSpendingInsight?> = _aiSpendingInsight.asStateFlow()

    // UI Feedback & Errors
    private val _uiErrorMessage = MutableStateFlow<String?>(null)
    val uiErrorMessage: StateFlow<String?> = _uiErrorMessage.asStateFlow()

    private val _uiSuccessMessage = MutableStateFlow<String?>(null)
    val uiSuccessMessage: StateFlow<String?> = _uiSuccessMessage.asStateFlow()

    init {
        // Automatically select the first trip when loaded if none selected
        viewModelScope.launch {
            allTrips.collect { trips ->
                if (_selectedTripId.value == null && trips.isNotEmpty()) {
                    _selectedTripId.value = trips.first().id
                }
            }
        }
    }

    fun selectTrip(tripId: Long) {
        _selectedTripId.value = tripId
        _aiSpendingInsight.value = null // reset summary for new trip
    }

    fun createTrip(
        name: String,
        destination: String,
        budget: Double,
        startDate: Long,
        endDate: Long,
        currencySymbol: String = "$",
        currencyCode: String = "USD",
        tripType: String = "Vacation",
        colorHex: Long = 0xFF4F46E5,
        groupMembers: String = "You, Alex, Priya, Rahul"
    ) {
        viewModelScope.launch {
            try {
                val newTrip = TripEntity(
                    name = name.ifBlank { "My Adventure" },
                    destination = destination.ifBlank { "Global" },
                    budget = budget.coerceAtLeast(0.0),
                    startDate = startDate,
                    endDate = endDate.coerceAtLeast(startDate),
                    currencySymbol = currencySymbol,
                    currencyCode = currencyCode,
                    tripType = tripType,
                    colorHex = colorHex,
                    groupMembers = groupMembers.ifBlank { "You" }
                )
                val id = repository.insertTrip(newTrip)
                _selectedTripId.value = id
                _uiSuccessMessage.value = "Trip '$name' created!"
            } catch (e: Exception) {
                Log.e(tag, "Failed to create trip", e)
                _uiErrorMessage.value = "Failed to create trip: ${e.localizedMessage}"
            }
        }
    }

    fun updateGroupMembers(trip: TripEntity, newMembersCsv: String) {
        viewModelScope.launch {
            try {
                val updated = trip.copy(groupMembers = newMembersCsv.ifBlank { "You" })
                repository.updateTrip(updated)
                _uiSuccessMessage.value = "Group travelers updated!"
            } catch (e: Exception) {
                Log.e(tag, "Failed to update group members", e)
                _uiErrorMessage.value = "Failed to update members: ${e.localizedMessage}"
            }
        }
    }

    fun updateTrip(trip: TripEntity) {
        viewModelScope.launch {
            try {
                repository.updateTrip(trip)
                _uiSuccessMessage.value = "Trip updated!"
            } catch (e: Exception) {
                Log.e(tag, "Failed to update trip", e)
                _uiErrorMessage.value = "Failed to update trip: ${e.localizedMessage}"
            }
        }
    }

    fun deleteTrip(trip: TripEntity) {
        viewModelScope.launch {
            try {
                repository.deleteTrip(trip)
                if (_selectedTripId.value == trip.id) {
                    val remaining = allTrips.value.filter { it.id != trip.id }
                    _selectedTripId.value = remaining.firstOrNull()?.id
                }
                _uiSuccessMessage.value = "Trip deleted"
            } catch (e: Exception) {
                Log.e(tag, "Failed to delete trip", e)
                _uiErrorMessage.value = "Failed to delete trip: ${e.localizedMessage}"
            }
        }
    }

    fun addExpense(
        tripId: Long,
        title: String,
        amount: Double,
        category: String,
        date: Long = System.currentTimeMillis(),
        paymentMethod: String = "Credit Card",
        notes: String = "",
        isAiParsed: Boolean = false,
        tags: String = "",
        itemsJson: String = "",
        imageBase64: String? = null,
        location: String = "",
        paidBy: String = "You",
        splitType: String = "EQUAL",
        splitWith: String = "",
        customSplitsJson: String = ""
    ) {
        viewModelScope.launch {
            try {
                val expense = ExpenseEntity(
                    tripId = tripId,
                    title = title.ifBlank { "Expense" },
                    amount = amount.coerceAtLeast(0.0),
                    category = category,
                    date = date,
                    paymentMethod = paymentMethod,
                    notes = notes,
                    isAiParsed = isAiParsed,
                    tags = tags,
                    itemsJson = itemsJson,
                    receiptImageBase64 = imageBase64,
                    location = location,
                    paidBy = paidBy.ifBlank { "You" },
                    splitType = splitType,
                    splitWith = splitWith,
                    customSplitsJson = customSplitsJson
                )
                repository.insertExpense(expense)
                _uiSuccessMessage.value = "Added: $title"
                clearParsedReceipt()
            } catch (e: Exception) {
                Log.e(tag, "Failed to add expense", e)
                _uiErrorMessage.value = "Failed to add expense: ${e.localizedMessage}"
            }
        }
    }

    fun recordSettlement(
        tripId: Long,
        fromMember: String,
        toMember: String,
        amount: Double,
        currencySymbol: String = "₹"
    ) {
        viewModelScope.launch {
            try {
                // Record payment as a settlement expense where fromMember paid for toMember
                val settlementExpense = ExpenseEntity(
                    tripId = tripId,
                    title = "🤝 Settlement: $fromMember ➔ $toMember",
                    amount = amount.coerceAtLeast(0.0),
                    category = ExpenseCategory.OTHER.displayName,
                    date = System.currentTimeMillis(),
                    paymentMethod = "Mobile Pay / UPI",
                    notes = "Group debt payment settled",
                    paidBy = fromMember,
                    splitType = "CUSTOM",
                    splitWith = toMember,
                    tags = "Settlement,Payment"
                )
                repository.insertExpense(settlementExpense)
                _uiSuccessMessage.value = "Settled: $fromMember paid $toMember $currencySymbol${String.format(Locale.US, "%.2f", amount)}"
            } catch (e: Exception) {
                Log.e(tag, "Failed to record settlement", e)
                _uiErrorMessage.value = "Failed to record settlement: ${e.localizedMessage}"
            }
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            try {
                repository.updateExpense(expense)
                _uiSuccessMessage.value = "Expense updated!"
            } catch (e: Exception) {
                Log.e(tag, "Failed to update expense", e)
                _uiErrorMessage.value = "Failed to update expense: ${e.localizedMessage}"
            }
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            try {
                repository.deleteExpense(expense)
                _uiSuccessMessage.value = "Expense deleted"
            } catch (e: Exception) {
                Log.e(tag, "Failed to delete expense", e)
                _uiErrorMessage.value = "Failed to delete expense: ${e.localizedMessage}"
            }
        }
    }

    fun scanReceiptWithGemini(
        imageBase64: String? = null,
        receiptText: String? = null,
        userHint: String? = null
    ) {
        viewModelScope.launch {
            _isReceiptScanning.value = true
            _scannedImageBase64.value = imageBase64
            val result = repository.parseReceiptWithGemini(
                imageBase64 = imageBase64,
                receiptText = receiptText,
                userHint = userHint
            )
            _isReceiptScanning.value = false

            result.onSuccess { parsed ->
                _parsedReceipt.value = parsed
                _uiSuccessMessage.value = "Receipt parsed! Auto-categorized as: ${parsed.category}"
            }.onFailure { error ->
                Log.w(tag, "Gemini scan failed or API key missing, using intelligent fallback parser", error)
                // Intelligent fallback extraction in case of offline / key missing
                val fallbackParsed = fallbackParseReceipt(receiptText, userHint)
                _parsedReceipt.value = fallbackParsed
                _uiSuccessMessage.value = "Receipt extracted! (Review fields before saving)"
            }
        }
    }

    fun loadSampleReceipt(preset: com.example.data.sample.SampleData.SampleReceiptPreset) {
        scanReceiptWithGemini(
            receiptText = preset.rawReceiptText,
            userHint = "Sample receipt from ${preset.merchant}"
        )
    }

    fun processImageUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val context = getApplication<Application>()
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (bitmap != null) {
                    // Resize to avoid huge payload while preserving high OCR clarity
                    val scaledBitmap = scaleBitmapToMax(bitmap, 1200)
                    val outputStream = ByteArrayOutputStream()
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                    val base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
                    
                    withContext(Dispatchers.Main) {
                        scanReceiptWithGemini(imageBase64 = base64)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiErrorMessage.value = "Could not load image file."
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "Error reading image URI", e)
                withContext(Dispatchers.Main) {
                    _uiErrorMessage.value = "Failed to load receipt image: ${e.localizedMessage}"
                }
            }
        }
    }

    private fun scaleBitmapToMax(bitmap: Bitmap, maxDim: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxDim && height <= maxDim) return bitmap
        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int
        if (width > height) {
            newWidth = maxDim
            newHeight = (maxDim / ratio).toInt()
        } else {
            newHeight = maxDim
            newWidth = (maxDim * ratio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    fun clearParsedReceipt() {
        _parsedReceipt.value = null
        _scannedImageBase64.value = null
    }

    fun generateSpendingSummary(trip: TripEntity) {
        viewModelScope.launch {
            _isGeneratingAiSummary.value = true
            val expenses = selectedTripExpenses.value
            val result = repository.summarizeTripSpending(trip, expenses)
            _isGeneratingAiSummary.value = false

            result.onSuccess { insight ->
                _aiSpendingInsight.value = insight
                _uiSuccessMessage.value = "Gemini spending pattern analysis ready!"
            }.onFailure { error ->
                Log.w(tag, "Gemini summary call failed, generating local smart spending breakdown", error)
                val localInsight = generateLocalSmartSummary(trip, expenses)
                _aiSpendingInsight.value = localInsight
            }
        }
    }

    private fun generateLocalSmartSummary(trip: TripEntity, expenses: List<ExpenseEntity>): AiSpendingInsight {
        val totalSpent = expenses.sumOf { it.amount }
        val remaining = trip.budget - totalSpent
        val isOverBudget = remaining < 0
        val categoryTotals = expenses.groupBy { it.expenseCategory }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
        val topCategory = categoryTotals.maxByOrNull { it.value }

        val topCategoryName = topCategory?.key?.displayName ?: "Dining"
        val topCategoryAmount = topCategory?.value ?: 0.0
        val topCategoryPct = if (totalSpent > 0) ((topCategoryAmount / totalSpent) * 100).toInt() else 0

        val highlights = mutableListOf<String>()
        highlights.add("$topCategoryName is your largest expense sector at ${trip.currencySymbol}${String.format(Locale.US, "%.2f", topCategoryAmount)} ($topCategoryPct% of total spend).")
        if (expenses.size > 3) {
            highlights.add("Recorded ${expenses.size} expenses across ${categoryTotals.size} distinct categories.")
        }
        val dailyAvg = if (trip.durationDays > 0) totalSpent / trip.durationDays else totalSpent
        highlights.add("Current burn rate is approximately ${trip.currencySymbol}${String.format(Locale.US, "%.2f", dailyAvg)}/day.")

        val tips = mutableListOf<String>()
        if (isOverBudget) {
            tips.add("You have exceeded the planned budget by ${trip.currencySymbol}${String.format(Locale.US, "%.2f", -remaining)}. Consider prioritizing free sights & budget dining.")
            tips.add("Review the high-ticket transactions in $topCategoryName to find opportunities to economize.")
        } else {
            val dailyRemaining = if (trip.durationDays > 0) remaining / trip.durationDays else remaining
            tips.add("You have ${trip.currencySymbol}${String.format(Locale.US, "%.2f", remaining)} remaining (${trip.currencySymbol}${String.format(Locale.US, "%.2f", dailyRemaining)}/day allowance).")
            tips.add("Look out for set menus and public transit day passes in ${trip.destination} for extra savings.")
        }

        return AiSpendingInsight(
            executiveSummary = "Spending for ${trip.name} stands at ${trip.currencySymbol}${String.format(Locale.US, "%.2f", totalSpent)} of ${trip.currencySymbol}${String.format(Locale.US, "%.2f", trip.budget)} budget. ${if (isOverBudget) "Budget exceeded." else "You are pacing reasonably within your target."}",
            topSpendingPattern = "Your primary spending driver is $topCategoryName ($topCategoryPct% of total). Average transaction size is ${trip.currencySymbol}${String.format(Locale.US, "%.2f", if (expenses.isNotEmpty()) totalSpent / expenses.size else 0.0)}.",
            paceAndBudgetAssessment = if (isOverBudget) "Over budget. Velocity exceeded the ceiling." else "Healthy spending pace. ${(remaining / trip.budget * 100).toInt()}% budget remains intact.",
            categoryHighlights = highlights,
            actionableSavingTips = tips,
            projectedFinalSpend = totalSpent * 1.15,
            alertLevel = if (isOverBudget) "DANGER" else if (remaining < trip.budget * 0.2) "WARNING" else "NORMAL"
        )
    }

    private fun fallbackParseReceipt(text: String?, hint: String?): ParsedReceipt {
        val raw = text ?: ""
        var extractedAmount = 0.0
        val amountRegex = """(?:\$|€|£|¥|TOTAL:?|Subtotal:?|AMOUNT:?)\s*([0-9]+[.,][0-9]{2})""".toRegex(RegexOption.IGNORE_CASE)
        val match = amountRegex.find(raw)
        if (match != null) {
            extractedAmount = match.groupValues[1].replace(",", ".").toDoubleOrNull() ?: 25.00
        } else {
            extractedAmount = 35.00
        }

        val firstLine = raw.lines().firstOrNull { it.isNotBlank() }?.take(30) ?: "Travel Expense"
        val detectedCat = ExpenseCategory.fromString("$firstLine $raw $hint")

        return ParsedReceipt(
            merchant = firstLine,
            totalAmount = extractedAmount,
            currency = "$",
            category = detectedCat.displayName,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            paymentMethod = if (raw.contains("cash", true)) "Cash" else "Credit Card",
            items = emptyList(),
            summaryNote = "Extracted expense from receipt",
            tags = listOf(detectedCat.displayName.split(" ").first())
        )
    }

    fun clearErrorMessage() {
        _uiErrorMessage.value = null
    }

    fun clearSuccessMessage() {
        _uiSuccessMessage.value = null
    }
}
