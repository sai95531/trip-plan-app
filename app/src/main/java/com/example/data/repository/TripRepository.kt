package com.example.data.repository

import com.example.data.db.ExpenseDao
import com.example.data.db.TripDao
import com.example.data.gemini.GeminiClient
import com.example.data.model.AiSpendingInsight
import com.example.data.model.ExpenseEntity
import com.example.data.model.ParsedReceipt
import com.example.data.model.TripEntity
import com.example.data.sample.SampleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TripRepository(
    private val tripDao: TripDao,
    private val expenseDao: ExpenseDao,
    private val geminiClient: GeminiClient = GeminiClient()
) {
    val allTrips: Flow<List<TripEntity>> = tripDao.getAllTrips()
    val allExpenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfNeeded()
        }
    }

    private suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        val existingTrips = tripDao.getAllTrips().firstOrNull()
        if (existingTrips.isNullOrEmpty()) {
            val initialTrips = SampleData.createInitialTrips()
            initialTrips.forEach { trip ->
                tripDao.insertTrip(trip)
            }
            val initialExpenses = SampleData.createInitialExpenses()
            initialExpenses.forEach { exp ->
                expenseDao.insertExpense(exp)
            }
        }
    }

    fun getExpensesForTrip(tripId: Long): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpensesForTrip(tripId)
    }

    fun getTripById(tripId: Long): Flow<TripEntity?> {
        return tripDao.getTripById(tripId)
    }

    suspend fun getTripByIdDirect(tripId: Long): TripEntity? {
        return tripDao.getTripByIdDirect(tripId)
    }

    suspend fun insertTrip(trip: TripEntity): Long {
        return tripDao.insertTrip(trip)
    }

    suspend fun updateTrip(trip: TripEntity) {
        tripDao.updateTrip(trip)
    }

    suspend fun deleteTrip(trip: TripEntity) {
        expenseDao.deleteExpensesForTrip(trip.id)
        tripDao.deleteTrip(trip)
    }

    suspend fun insertExpense(expense: ExpenseEntity): Long {
        return expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }

    suspend fun parseReceiptWithGemini(
        imageBase64: String? = null,
        receiptText: String? = null,
        userHint: String? = null
    ): Result<ParsedReceipt> {
        return geminiClient.parseReceipt(
            imageBase64 = imageBase64,
            receiptText = receiptText,
            userHint = userHint
        )
    }

    suspend fun summarizeTripSpending(
        trip: TripEntity,
        expenses: List<ExpenseEntity>
    ): Result<AiSpendingInsight> {
        return geminiClient.summarizeSpendingPatterns(trip, expenses)
    }
}
