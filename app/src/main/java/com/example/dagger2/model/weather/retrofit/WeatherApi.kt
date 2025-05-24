package com.example.dagger2.model.weather.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") coordinates: String,
        @Query("lang") language: String = "ru"
    ): Weather
}