package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.ExpenseCategory
import com.example.data.model.ExpenseEntity
import com.example.data.model.TripEntity
import com.example.ui.components.CategoryDistributionChart
import com.example.ui.components.TripHeroCard
import com.example.ui.theme.MyApplicationTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun trip_hero_card_screenshot() {
        val trip = TripEntity(
            name = "Kyoto & Tokyo Autumn",
            destination = "Japan",
            budget = 2500.0,
            startDate = 1725000000000L,
            endDate = 1725604800000L,
            currencySymbol = "$",
            currencyCode = "USD"
        )
        val sampleExpenses = listOf(
            ExpenseEntity(tripId = trip.id, title = "Ippudo Ramen", amount = 38.50, category = "Food & Dining"),
            ExpenseEntity(tripId = trip.id, title = "Shinkansen Bullet Train", amount = 142.00, category = "Transportation"),
            ExpenseEntity(tripId = trip.id, title = "Ryokan Lodging", amount = 420.00, category = "Lodging")
        )

        composeTestRule.setContent {
            MyApplicationTheme {
                TripHeroCard(
                    trip = trip,
                    expenses = sampleExpenses,
                    onViewSummary = {},
                    onEditTrip = {},
                    onDeleteTrip = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/trip_hero.png")
    }
}
