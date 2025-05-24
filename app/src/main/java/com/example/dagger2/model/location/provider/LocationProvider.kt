package com.example.dagger2.model.location.provider

import com.example.dagger2.model.location.LocationResult


interface LocationProvider {
    suspend fun getLocation(timeoutMillis: Long): LocationResult
}