package com.example.dagger2.model.weather.repository

import com.example.dagger2.model.weather.WeatherResult
import com.example.dagger2.model.weather.retrofit.WeatherApi

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi
): WeatherRepository {

    override suspend fun getCurrentWeather(coordinate: String): WeatherResult {
        try {
            val response = weatherApi.getCurrentWeather(coordinate)
            return WeatherResult.Success(response)
        } catch (_: Exception) {
            return WeatherResult.TechnicalError
        }
    }
}