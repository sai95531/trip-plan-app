package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.explore.DestinationCatalog
import com.example.data.explore.DynamicDestinationEngine
import com.example.data.gemini.GeminiClient
import com.example.data.location.LocationManager
import com.example.data.model.DestinationExploreData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class DestinationExploreViewModel(application: Application) : AndroidViewModel(application) {
    private val geminiClient = GeminiClient()

    val searchQuery = MutableStateFlow("Bangalore")
    val selectedCategoryFilter = MutableStateFlow("All")
    val selectedDietFilter = MutableStateFlow("All")

    private val _isDetectingLocation = MutableStateFlow(false)
    val isDetectingLocation: StateFlow<Boolean> = _isDetectingLocation.asStateFlow()

    private val _currentLocationCity = MutableStateFlow<String?>(null)
    val currentLocationCity: StateFlow<String?> = _currentLocationCity.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isEnrichingOnline = MutableStateFlow(false)
    val isEnrichingOnline: StateFlow<Boolean> = _isEnrichingOnline.asStateFlow()

    private val _destinationData = MutableStateFlow<DestinationExploreData?>(null)
    val destinationData: StateFlow<DestinationExploreData?> = _destinationData.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // In-memory instant cache for zero latency switching
    private val exploreCache = ConcurrentHashMap<String, DestinationExploreData>()
    private var loadJob: Job? = null

    val popularDestinations = DestinationCatalog.popularDestinations

    val placeCategories = listOf(
        "All",
        "Nature",
        "Historical",
        "Instagrammable",
        "Viewpoint",
        "Cultural",
        "Nightlife",
        "Spiritual",
        "Market",
        "Adventure"
    )
    val foodDietFilters = listOf("All", "Vegetarian", "Non-Veg", "Sweet/Dessert", "Beverage", "Street Food")

    private val locationManager = LocationManager.getInstance(application)

    init {
        // Pre-populate popular catalog items into memory cache for instant access
        viewModelScope.launch(Dispatchers.Default) {
            popularDestinations.forEach { city ->
                DestinationCatalog.getDestination(city)?.let {
                    exploreCache[city.lowercase()] = it
                }
            }
        }

        // Instant display for initial destination
        selectDestination("Bangalore")
    }

    fun selectDestination(dest: String) {
        val cleanDest = dest.trim().ifBlank { "Bangalore" }
        searchQuery.value = cleanDest
        loadExploreData(cleanDest)
    }

    fun detectAndExploreCurrentLocation() {
        viewModelScope.launch {
            _isDetectingLocation.value = true
            val loc = locationManager.getCurrentUserLocation()
            _isDetectingLocation.value = false
            if (loc != null && loc.cityName.isNotBlank()) {
                _currentLocationCity.value = loc.cityName
                searchQuery.value = loc.cityName
                loadExploreData(loc.cityName, "${loc.cityName}, ${loc.stateOrRegion}")
            } else {
                loadExploreData(searchQuery.value)
            }
        }
    }

    fun loadExploreData(destinationName: String, locationHint: String = "") {
        val dest = destinationName.trim().ifBlank { "Bangalore" }
        val key = dest.lowercase()

        // 1. Instant Cache or Local Catalog Resolution (0ms delay)
        val cached = exploreCache[key]
        if (cached != null) {
            _destinationData.value = cached
            _isLoading.value = false
            return
        }

        val catalogItem = DestinationCatalog.getDestination(dest)
        if (catalogItem != null) {
            exploreCache[key] = catalogItem
            _destinationData.value = catalogItem
            _isLoading.value = false
            return
        }

        // 2. Generate immediate dynamic baseline for non-catalog places so user never waits
        val instantBaseline = DynamicDestinationEngine.generate(dest)
        _destinationData.value = instantBaseline
        exploreCache[key] = instantBaseline
        _isLoading.value = false

        // 3. Online AI Background Enrichment (non-blocking)
        loadJob?.cancel()
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            _isEnrichingOnline.value = true
            _errorMessage.value = null

            val result = geminiClient.getFamousPlacesAndFoods(dest, locationHint)
            _isEnrichingOnline.value = false

            result.onSuccess { enriched ->
                if (enriched.famousPlaces.isNotEmpty() || enriched.localFoods.isNotEmpty()) {
                    exploreCache[key] = enriched
                    withContext(Dispatchers.Main) {
                        _destinationData.value = enriched
                    }
                }
            }
        }
    }
}

