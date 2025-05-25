package com.example.dagger2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dagger2.model.location.repository.LocationRepository
import com.example.dagger2.model.location.LocationResult
import com.example.dagger2.model.weather.repository.WeatherRepository
import com.example.dagger2.model.weather.WeatherResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
): ViewModel() {

    private val _location = MutableLiveData<LocationResult>()
    val location: LiveData<LocationResult> get() = _location
    private val _currentWeather = MutableLiveData<WeatherResult>()
    val currentWeather get() = _currentWeather

    init {
        getLocation()
    }

    fun getLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val locationResult = locationRepository.getLocation()
            _location.postValue(locationResult)
        }
    }

    fun getWeather(coordinate: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherResult = weatherRepository.getCurrentWeather(coordinate)
            _currentWeather.postValue(weatherResult)
        }
    }
}