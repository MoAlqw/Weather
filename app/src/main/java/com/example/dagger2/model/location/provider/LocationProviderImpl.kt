package com.example.dagger2.model.location.provider

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.dagger2.model.location.Coordinates
import com.example.dagger2.model.location.LocationResult
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationProviderImpl(context: Context): LocationProvider {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation(): Location? = suspendCoroutine { cont ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { cont.resume(it) }
            .addOnFailureListener { cont.resume(null) }
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestLocationUpdateOnce(): LocationResult =
        suspendCancellableCoroutine { cont ->
            val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000L)
                .setMaxUpdates(1)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                    val location = result.locations.firstOrNull()
                    if (location != null) {
                        val locationResult = Coordinates(location.longitude.toLong(), location.latitude.toLong())
                        cont.resume(LocationResult.Success(locationResult))
                    } else {
                        cont.resume(LocationResult.NotAvailable)
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
            cont.invokeOnCancellation { fusedLocationClient.removeLocationUpdates(callback) }
        }

    override suspend fun getLocation(timeoutMillis: Long): LocationResult {
        val lastKnownLocation = getLastLocation()
        if (lastKnownLocation != null) {
            return LocationResult.Success(Coordinates(
                lastKnownLocation.longitude.toLong(), lastKnownLocation.latitude.toLong()
            ))
        }

        return withTimeoutOrNull(timeoutMillis) {
            requestLocationUpdateOnce()
        } ?:LocationResult.NotAvailable
    }
}