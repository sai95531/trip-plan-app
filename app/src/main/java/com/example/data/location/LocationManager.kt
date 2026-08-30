package com.example.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale
import kotlin.coroutines.resume

data class UserLocationInfo(
    val cityName: String,
    val stateOrRegion: String,
    val country: String,
    val formattedAddress: String,
    val latitude: Double,
    val longitude: Double,
    val isGpsDerived: Boolean
)

data class LocationAiContext(
    val cityName: String,
    val stateOrRegion: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val promptContextString: String
)

class LocationManager(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    companion object {
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        @Volatile
        private var instance: LocationManager? = null

        fun getInstance(context: Context): LocationManager {
            return instance ?: synchronized(this) {
                instance ?: LocationManager(context.applicationContext).also { instance = it }
            }
        }
    }

    /**
     * Checks whether the application has been granted location permissions.
     */
    fun hasLocationPermission(): Boolean {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineGranted || coarseGranted
    }

    /**
     * Fetches current GPS location using Google Play Services FusedLocationProviderClient
     * with high accuracy and reverse-geocodes it into structured UserLocationInfo.
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentUserLocation(): UserLocationInfo? = withContext(Dispatchers.IO) {
        if (!hasLocationPermission()) {
            return@withContext null
        }

        try {
            // First try high-accuracy current location with timeout
            val freshLocation = withTimeoutOrNull(10000L) {
                suspendCancellableCoroutine<Location?> { cont ->
                    val cts = CancellationTokenSource()
                    cont.invokeOnCancellation { cts.cancel() }
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        cts.token
                    ).addOnSuccessListener { loc ->
                        if (cont.isActive) cont.resume(loc)
                    }.addOnFailureListener {
                        if (cont.isActive) cont.resume(null)
                    }
                }
            }

            if (freshLocation != null) {
                return@withContext resolveAddress(freshLocation.latitude, freshLocation.longitude, isGps = true)
            }

            // Fallback to last known location
            val lastLocation = suspendCancellableCoroutine<Location?> { cont ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { loc ->
                        if (cont.isActive) cont.resume(loc)
                    }
                    .addOnFailureListener {
                        if (cont.isActive) cont.resume(null)
                    }
            }

            if (lastLocation != null) {
                return@withContext resolveAddress(lastLocation.latitude, lastLocation.longitude, isGps = true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext null
    }

    /**
     * Formats and provides the user's current location to AI services (Gemini Itinerary, Destination Guide, etc.)
     */
    suspend fun provideLocationToAi(): LocationAiContext? = withContext(Dispatchers.IO) {
        val locInfo = getCurrentUserLocation() ?: return@withContext null
        val promptContext = buildString {
            append("User Current Location: ${locInfo.cityName}, ${locInfo.stateOrRegion}, ${locInfo.country}")
            append(" (Lat: %.4f, Lng: %.4f)".format(Locale.US, locInfo.latitude, locInfo.longitude))
        }
        LocationAiContext(
            cityName = locInfo.cityName,
            stateOrRegion = locInfo.stateOrRegion,
            country = locInfo.country,
            latitude = locInfo.latitude,
            longitude = locInfo.longitude,
            promptContextString = promptContext
        )
    }

    /**
     * Reverse-geocodes lat/lng into a descriptive address with city, state, country.
     */
    suspend fun resolveAddress(
        lat: Double,
        lng: Double,
        isGps: Boolean
    ): UserLocationInfo = withContext(Dispatchers.IO) {
        try {
            if (Geocoder.isPresent()) {
                val geocoder = Geocoder(context, Locale.getDefault())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val addresses = geocoder.getFromLocation(lat, lng, 1)
                    if (!addresses.isNullOrEmpty()) {
                        return@withContext parseAddress(addresses[0], lat, lng, isGps)
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(lat, lng, 1)
                    if (!addresses.isNullOrEmpty()) {
                        return@withContext parseAddress(addresses[0], lat, lng, isGps)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fallback default
        return@withContext UserLocationInfo(
            cityName = "Goa",
            stateOrRegion = "Goa",
            country = "India",
            formattedAddress = "India (%.3f, %.3f)".format(Locale.US, lat, lng),
            latitude = lat,
            longitude = lng,
            isGpsDerived = isGps
        )
    }

    private fun parseAddress(
        addr: Address,
        lat: Double,
        lng: Double,
        isGps: Boolean
    ): UserLocationInfo {
        val city = addr.locality
            ?: addr.subAdminArea
            ?: addr.adminArea
            ?: "Nearby City"
        val state = addr.adminArea ?: "India"
        val country = addr.countryName ?: "India"
        val full = (0..addr.maxAddressLineIndex).joinToString(", ") { addr.getAddressLine(it) }
            .ifBlank { "$city, $state, $country" }

        return UserLocationInfo(
            cityName = city,
            stateOrRegion = state,
            country = country,
            formattedAddress = full,
            latitude = lat,
            longitude = lng,
            isGpsDerived = isGps
        )
    }
}
