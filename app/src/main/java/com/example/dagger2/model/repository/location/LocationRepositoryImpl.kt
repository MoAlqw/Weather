package com.example.dagger2.model.repository.location

class LocationRepositoryImpl(
    private val locationHelper: LocationHelper
): LocationRepository {

    override suspend fun getLocation(): LocationResult {
        if (!locationHelper.isGpsOn()) return LocationResult.GpsOff
        if (!locationHelper.hasPermission()) return LocationResult.NoPermission
        return locationHelper.getLocation()
    }
}