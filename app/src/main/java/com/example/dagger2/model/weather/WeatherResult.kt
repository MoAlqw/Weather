package com.example.dagger2.model.weather

import com.example.dagger2.model.weather.retrofit.Weather

sealed class WeatherResult {
    data class Success(val data: Weather) : WeatherResult()
    object TechnicalError : WeatherResult()
}