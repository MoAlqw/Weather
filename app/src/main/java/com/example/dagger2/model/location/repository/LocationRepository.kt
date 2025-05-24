package com.example.dagger2.model.location.repository

import com.example.dagger2.model.location.LocationResult

interface LocationRepository {
    suspend fun getLocation(): LocationResult
}