package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.ExpenseCategory
import com.example.data.model.ExpenseEntity
import com.example.data.model.TripEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Trip Expense", appName)
    }

    @Test
    fun `test expense category auto-detection`() {
        val flightCat = ExpenseCategory.fromString("Flight to Tokyo Delta Airlines")
        assertEquals(ExpenseCategory.FLIGHTS, flightCat)

        val foodCat = ExpenseCategory.fromString("Ippudo Ramen Kyoto Dinner")
        assertEquals(ExpenseCategory.FOOD_DINING, foodCat)

        val hotelCat = ExpenseCategory.fromString("Marriott Hotel Kyoto Booking")
        assertEquals(ExpenseCategory.LODGING, hotelCat)

        val cafeCat = ExpenseCategory.fromString("Starbucks Coffee Matcha Latte")
        assertEquals(ExpenseCategory.CAFE_SNACKS, cafeCat)
    }

    @Test
    fun `test trip entity and expense calculations`() {
        val trip = TripEntity(
            name = "Japan Trip",
            destination = "Kyoto",
            budget = 2000.0,
            startDate = 1000L,
            endDate = 1000L + (7 * 24 * 60 * 60 * 1000L)
        )
        assertEquals(7, trip.durationDays)

        val exp1 = ExpenseEntity(tripId = trip.id, title = "Dinner", amount = 85.0, category = "Food & Dining")
        val exp2 = ExpenseEntity(tripId = trip.id, title = "Train", amount = 45.0, category = "Transportation")

        val total = exp1.amount + exp2.amount
        assertEquals(130.0, total, 0.01)
        assertTrue(trip.budget > total)
    }
}
