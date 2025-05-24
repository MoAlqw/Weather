package com.example.dagger2.model.location.gps

import com.example.dagger2.model.location.LocationResult

interface GpsStatusChecker {
    suspend fun checkGpsStatus(): LocationResult
}