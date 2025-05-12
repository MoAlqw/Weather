package com.example.dagger2

import android.app.Application
import com.example.dagger2.model.repository.location.LocationHelper
import com.example.dagger2.model.repository.location.LocationRepository
import com.example.dagger2.model.repository.location.LocationRepositoryImpl
import com.example.dagger2.model.repository.weather.WeatherRepository
import com.example.dagger2.model.repository.weather.WeatherRepositoryImpl
import com.example.dagger2.model.repository.weather.retrofit.AuthInterceptor
import com.example.dagger2.model.repository.weather.retrofit.WeatherApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApp: Application() {

    lateinit var weatherRepository: WeatherRepository
    lateinit var locationRepository: LocationRepository

    override fun onCreate() {
        super.onCreate()
        weatherRepository = createWeatherRepository()
        locationRepository = createLocationRepository()
    }

    private fun createLocationRepository(): LocationRepository {
        return LocationRepositoryImpl(LocationHelper(applicationContext))
    }

    private fun createWeatherRepository(): WeatherRepository {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return WeatherRepositoryImpl(retrofit.create(WeatherApi::class.java))
    }
}