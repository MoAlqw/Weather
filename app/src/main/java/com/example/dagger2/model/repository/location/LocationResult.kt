package com.example.dagger2.model.repository.location

import android.location.Location

sealed class LocationResult {
    data class Success(val location: Location) : LocationResult()
    object NoPermission : LocationResult()
    object NotAvailable : LocationResult()
    object GpsOff : LocationResult()
}
