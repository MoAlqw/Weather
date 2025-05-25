package com.example.dagger2.model.location.gps

import android.content.Context
import com.example.dagger2.model.location.LocationResult
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GpsStatusCheckerImpl(context: Context): GpsStatusChecker {
    private val settingsClient = LocationServices.getSettingsClient(context)

    override suspend fun checkGpsStatus(): LocationResult = suspendCoroutine { cont ->
        val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 500L).build()
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(request)
            .setAlwaysShow(true)
            .build()

        settingsClient.checkLocationSettings(settingsRequest)
            .addOnSuccessListener { cont.resume(LocationResult.GpsOn) }
            .addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    cont.resume(LocationResult.GpsResolutionRequired(AndroidGpsResolution(e)))
                } else {
                    cont.resume(LocationResult.GpsOff)
                }
            }
    }
}