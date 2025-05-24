package com.example.dagger2

import android.app.Application
import com.example.dagger2.model.location.gps.GpsStatusCheckerImpl
import com.example.dagger2.model.location.permission.PermissionCheckerImpl
import com.example.dagger2.model.location.provider.LocationProviderImpl
import com.example.dagger2.model.location.repository.LocationRepository
import com.example.dagger2.model.location.repository.LocationRepositoryImpl
import com.example.dagger2.model.weather.repository.WeatherRepository
import com.example.dagger2.model.weather.repository.WeatherRepositoryImpl
import com.example.dagger2.model.weather.retrofit.AuthInterceptor
import com.example.dagger2.model.weather.retrofit.WeatherApi
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
        val permissionChecker = PermissionCheckerImpl(applicationContext)
        val gpsChecker = GpsStatusCheckerImpl(applicationContext)
        val locationProvider = LocationProviderImpl(applicationContext)
        return LocationRepositoryImpl(
            permissionChecker,
            gpsChecker,
            locationProvider
        )
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