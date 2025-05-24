package com.example.dagger2.model.location.repository

import com.example.dagger2.model.location.LocationResult
import com.example.dagger2.model.location.gps.GpsStatusChecker
import com.example.dagger2.model.location.permission.LocationPermissionChecker
import com.example.dagger2.model.location.provider.LocationProvider

class LocationRepositoryImpl(
    private val locationPermissionChecker: LocationPermissionChecker,
    private val gpsStatusChecker: GpsStatusChecker,
    private val locationProvider: LocationProvider
): LocationRepository {

    override suspend fun getLocation(): LocationResult {
        if (!locationPermissionChecker.hasPermission()) return LocationResult.NoPermission

        return when (val gpsStatus = gpsStatusChecker.checkGpsStatus()) {
            is LocationResult.GpsOn -> locationProvider.getLocation(5_000L)
            else -> gpsStatus
        }
    }
}