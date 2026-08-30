package com.example.data.location

import android.content.Context

/**
 * Backward-compatible helper that delegates to [LocationManager].
 */
object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        return LocationManager.getInstance(context).hasLocationPermission()
    }

    suspend fun getCurrentLocation(context: Context): UserLocationInfo? {
        return LocationManager.getInstance(context).getCurrentUserLocation()
    }

    suspend fun resolveAddressFromCoordinates(
        context: Context,
        lat: Double,
        lng: Double,
        isGps: Boolean
    ): UserLocationInfo {
        return LocationManager.getInstance(context).resolveAddress(lat, lng, isGps)
    }
}
