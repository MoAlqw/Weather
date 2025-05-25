package com.example.dagger2.model.location.gps

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest

interface GpsResolution {
    fun launch(launcher: ActivityResultLauncher<IntentSenderRequest>)
}