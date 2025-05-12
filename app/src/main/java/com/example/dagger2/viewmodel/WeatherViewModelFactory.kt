package com.example.dagger2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dagger2.model.repository.location.LocationHelper
import com.example.dagger2.model.repository.location.LocationRepository
import com.example.dagger2.model.repository.weather.WeatherRepository

class WeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            return WeatherViewModel(weatherRepository, locationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

}