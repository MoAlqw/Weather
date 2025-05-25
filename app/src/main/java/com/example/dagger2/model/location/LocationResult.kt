package com.example.dagger2.model.location

import com.example.dagger2.model.location.gps.GpsResolution

sealed class LocationResult {
    data class Success(val location: Coordinates) : LocationResult()
    object NoPermission : LocationResult()
    object NotAvailable : LocationResult()
    object GpsOff : LocationResult()
    object GpsOn : LocationResult()
    data class GpsResolutionRequired(val resolution: GpsResolution) : LocationResult()
}

