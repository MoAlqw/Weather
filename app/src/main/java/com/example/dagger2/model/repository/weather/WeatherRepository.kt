package com.example.dagger2.model.repository.weather

interface WeatherRepository {

    suspend fun getCurrentWeather(coordinate: String): WeatherResult

}