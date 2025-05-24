package com.example.dagger2.model.weather.repository

import com.example.dagger2.model.weather.WeatherResult

interface WeatherRepository {

    suspend fun getCurrentWeather(coordinate: String): WeatherResult

}