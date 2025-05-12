package com.example.dagger2.model.repository.weather

import com.example.dagger2.model.repository.weather.retrofit.Weather

sealed class WeatherResult {
    data class Success(val data: Weather) : WeatherResult()
    object TechnicalError : WeatherResult()
}