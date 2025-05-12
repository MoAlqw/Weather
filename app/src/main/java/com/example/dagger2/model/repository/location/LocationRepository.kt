package com.example.dagger2.model.repository.location

interface LocationRepository {

    suspend fun getLocation(): LocationResult
}