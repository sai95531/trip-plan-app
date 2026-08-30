package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.gemini.GeminiClient
import com.example.data.location.LocationManager
import com.example.data.model.GeneratedItinerary
import com.example.data.model.LeaveTripOption
import com.example.data.model.TripEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TripPlannerViewModel(application: Application) : AndroidViewModel(application) {
    private val geminiClient = GeminiClient()
    private val locationManager = LocationManager.getInstance(application)

    // Form inputs
    val destination = MutableStateFlow("Goa")
    val leaveDays = MutableStateFlow(2) // 2 leaves = 4 day long weekend trip
    val weekendBridge = MutableStateFlow("Weekend + 2 Leaves (4 Days)")
    val travelVibe = MutableStateFlow("Beach, Cafe & Sunset Vibe")
    val companionType = MutableStateFlow("Friends Gang")
    val budgetInr = MutableStateFlow(25000.0)
    val startingCity = MutableStateFlow("Mumbai")
    val specialPreferences = MutableStateFlow("")

    // Calendar state
    val selectedStartDateMillis = MutableStateFlow<Long?>(null)
    val selectedEndDateMillis = MutableStateFlow<Long?>(null)
    val selectedLeaveDatesMillis = MutableStateFlow<Set<Long>>(emptySet())
    val formattedDateRange = MutableStateFlow("Upcoming Long Weekend (Fri - Mon)")
    val isCustomCalendarActive = MutableStateFlow(false)

    // Location state
    private val _currentLocationName = MutableStateFlow<String?>(null)
    val currentLocationName: StateFlow<String?> = _currentLocationName.asStateFlow()

    private val _isDetectingLocation = MutableStateFlow(false)
    val isDetectingLocation: StateFlow<Boolean> = _isDetectingLocation.asStateFlow()

    // Generated Itinerary
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _generatedItinerary = MutableStateFlow<GeneratedItinerary?>(null)
    val generatedItinerary: StateFlow<GeneratedItinerary?> = _generatedItinerary.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val popularDestinations = listOf(
        "Goa",
        "Manali, Himachal",
        "Jaipur, Rajasthan",
        "Munnar & Alleppey, Kerala",
        "Ooty & Coonoor",
        "Rishikesh & Haridwar",
        "Varanasi, UP",
        "Leh Ladakh",
        "Udaipur, Rajasthan",
        "Pondicherry",
        "Darjeeling & Gangtok",
        "Andaman Islands",
        "Coorg, Karnataka",
        "Meghalaya (Shillong & Cherrapunji)",
        "Dubai, UAE",
        "Bali, Indonesia",
        "Bangkok & Phuket, Thailand"
    )

    val travelVibes = listOf(
        "Beach, Cafe & Sunset Vibe",
        "Snow & Mountain Serenity",
        "Royal Heritage & Forts",
        "Adventure & River Rafting",
        "Backpacking & Budget Travel",
        "Culinary & Street Food Trail",
        "Spiritual & Wellness Retreat",
        "Luxury Resort & Spa Staycation"
    )

    val companionTypes = listOf(
        "Solo Explorer",
        "Couple Romantic",
        "Friends Gang",
        "Family with Kids & Elders",
        "Office Colleagues Offsite"
    )

    val leaveOptions = listOf(
        LeaveTripOption(
            leaveDays = 1,
            totalDays = 3,
            title = "1 Leave + Weekend",
            description = "Friday or Monday off = 3-Day Quick Escape",
            badge = "Quick Getaway"
        ),
        LeaveTripOption(
            leaveDays = 2,
            totalDays = 4,
            title = "2 Leaves + Weekend",
            description = "Thu-Sun or Fri-Mon = 4-Day Perfect Roadtrip",
            badge = "Most Popular"
        ),
        LeaveTripOption(
            leaveDays = 3,
            totalDays = 5,
            title = "3 Leaves + Weekend",
            description = "Wed-Sun = 5-Day Scenic Hill Station or Beach Trip",
            badge = "Sweet Spot"
        ),
        LeaveTripOption(
            leaveDays = 4,
            totalDays = 9,
            title = "4 Leaves + 2 Weekends",
            description = "Mon-Thu off across 2 weekends = 9-Day Grand Vacation",
            badge = "Mega Explorer"
        )
    )

    init {
        // Initialize default dates (upcoming Friday to Monday)
        initDefaultWeekendDates()
    }

    private fun initDefaultWeekendDates() {
        val cal = Calendar.getInstance()
        // Find next Friday
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        val startMillis = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 3) // Monday
        val endMillis = cal.timeInMillis

        setDateRange(startMillis, endMillis, isUserExplicit = false)
    }

    /**
     * Sets the trip date range from the Calendar picker and automatically calculates
     * working leaves needed vs weekend days.
     */
    fun setDateRange(startMillis: Long, endMillis: Long, isUserExplicit: Boolean = true) {
        val start = minOf(startMillis, endMillis)
        val end = maxOf(startMillis, endMillis)

        selectedStartDateMillis.value = start
        selectedEndDateMillis.value = end
        if (isUserExplicit) {
            isCustomCalendarActive.value = true
        }

        // Iterate days to count weekdays (leaves) and weekend days
        val cal = Calendar.getInstance().apply { timeInMillis = start }
        val endCal = Calendar.getInstance().apply { timeInMillis = end }

        var totalCount = 0
        var workingLeavesCount = 0
        var weekendDaysCount = 0
        val leavesSet = mutableSetOf<Long>()

        while (cal.timeInMillis <= endCal.timeInMillis || isSameDay(cal, endCal)) {
            totalCount++
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                weekendDaysCount++
            } else {
                workingLeavesCount++
                leavesSet.add(cal.timeInMillis)
            }
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        val finalTotal = maxOf(1, totalCount)
        val finalLeaves = if (workingLeavesCount == 0 && finalTotal > 0) 0 else maxOf(1, workingLeavesCount)
        leaveDays.value = finalLeaves
        selectedLeaveDatesMillis.value = leavesSet

        val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        val startStr = sdf.format(Date(start))
        val endStr = sdf.format(Date(end))

        formattedDateRange.value = if (startStr == endStr) {
            "$startStr (1 Day Trip • $finalLeaves Leave)"
        } else {
            "$startStr - $endStr ($finalTotal Days • $finalLeaves Leaves + $weekendDaysCount Weekend Days)"
        }

        weekendBridge.value = "$finalTotal Days Vacation ($finalLeaves Leaves taken)"
    }

    /**
     * Selects specific individual leave dates on the calendar and auto-bridges nearby weekends.
     */
    fun toggleLeaveDate(dateMillis: Long) {
        isCustomCalendarActive.value = true
        val currentSet = selectedLeaveDatesMillis.value.toMutableSet()
        if (currentSet.contains(dateMillis)) {
            currentSet.remove(dateMillis)
        } else {
            currentSet.add(dateMillis)
        }
        selectedLeaveDatesMillis.value = currentSet

        if (currentSet.isEmpty()) {
            leaveDays.value = 1
            formattedDateRange.value = "1 Day Quick Leave"
            return
        }

        val sorted = currentSet.sorted()
        val minDate = sorted.first()
        val maxDate = sorted.last()

        setDateRange(minDate, maxDate, isUserExplicit = true)
    }

    fun selectPresetOption(option: LeaveTripOption) {
        isCustomCalendarActive.value = false
        leaveDays.value = option.leaveDays
        weekendBridge.value = option.title

        // Update synthetic dates for display
        val cal = Calendar.getInstance()
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        val startMillis = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, option.totalDays - 1)
        val endMillis = cal.timeInMillis

        val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        formattedDateRange.value = "${sdf.format(Date(startMillis))} - ${sdf.format(Date(endMillis))} (${option.totalDays} Days • ${option.leaveDays} Leaves)"
    }

    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun calculateTotalTripDays(): Int {
        val start = selectedStartDateMillis.value
        val end = selectedEndDateMillis.value
        if (isCustomCalendarActive.value && start != null && end != null) {
            val diff = maxOf(0L, end - start)
            val days = (diff / (1000 * 60 * 60 * 24)).toInt() + 1
            return maxOf(1, days)
        }

        val leaves = leaveDays.value
        return when (leaves) {
            1 -> 3
            2 -> 4
            3 -> 5
            4 -> 9
            5 -> 10
            else -> leaves + 2
        }
    }

    fun detectCurrentLocation() {
        viewModelScope.launch {
            _isDetectingLocation.value = true
            val loc = locationManager.getCurrentUserLocation()
            _isDetectingLocation.value = false
            loc?.let {
                _currentLocationName.value = it.cityName
                if (startingCity.value.isBlank() || startingCity.value == "Mumbai") {
                    startingCity.value = it.cityName
                }
            }
        }
    }

    fun generateItinerary() {
        viewModelScope.launch {
            _isGenerating.value = true
            _errorMessage.value = null
            val totalDays = calculateTotalTripDays()

            val datesDesc = formattedDateRange.value

            val result = geminiClient.planTripByLeaves(
                destination = destination.value.ifBlank { "Goa" },
                leaveDays = leaveDays.value,
                totalDays = totalDays,
                travelVibe = travelVibe.value,
                companionType = companionType.value,
                budgetInr = budgetInr.value,
                startingCity = startingCity.value,
                preferences = specialPreferences.value,
                datesDescription = datesDesc
            )

            _isGenerating.value = false
            result.onSuccess {
                _generatedItinerary.value = it
            }.onFailure {
                _errorMessage.value = "Failed to generate plan: ${it.localizedMessage}"
            }
        }
    }

    fun clearItinerary() {
        _generatedItinerary.value = null
    }

    fun buildTripEntityFromItinerary(itinerary: GeneratedItinerary): TripEntity {
        val startMillis = selectedStartDateMillis.value ?: System.currentTimeMillis()
        val cal = Calendar.getInstance().apply {
            timeInMillis = startMillis
            add(Calendar.DAY_OF_YEAR, itinerary.totalDays)
        }
        val endMillis = selectedEndDateMillis.value ?: cal.timeInMillis

        return TripEntity(
            name = itinerary.tripTitle,
            destination = itinerary.destination,
            budget = itinerary.estimatedTotalBudgetInr,
            startDate = startMillis,
            endDate = endMillis,
            currencySymbol = "₹",
            currencyCode = "INR",
            tripType = itinerary.travelVibe.take(20),
            colorHex = 0xFF0D9488
        )
    }
}
