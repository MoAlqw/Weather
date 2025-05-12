package com.example.dagger2.model.repository.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlin.coroutines.resume
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

class LocationHelper(private val context: Context) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val settingsClient = LocationServices.getSettingsClient(context)

    fun hasPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    suspend fun isGpsOn(): Boolean = suspendCoroutine { cont ->
        val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 500L).build()
        val settingsRequest = LocationSettingsRequest.Builder().addLocationRequest(request).build()
        settingsClient.checkLocationSettings(settingsRequest)
            .addOnSuccessListener { cont.resume(true) }
            .addOnFailureListener { cont.resume(false) }
    }

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): LocationResult = suspendCoroutine { cont ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) cont.resume(LocationResult.Success(location))
                else getCurrentLocation(cont)
            }
            .addOnFailureListener {
                cont.resume(LocationResult.NotAvailable)
            }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(cont: Continuation<LocationResult>) {
        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()

        fusedLocationClient.getCurrentLocation(request, null)
            .addOnSuccessListener { location ->
                if (location != null) cont.resume(LocationResult.Success(location))
                else cont.resume(LocationResult.NotAvailable)
            }
            .addOnFailureListener {
                cont.resume(LocationResult.NotAvailable)
            }
    }
}
