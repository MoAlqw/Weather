package com.example.dagger2.model.location.gps

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException

class AndroidGpsResolution(private val resolvable: ResolvableApiException): GpsResolution {
    override fun launch(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        val intentSenderRequest = IntentSenderRequest.Builder(resolvable.resolution).build()
        launcher.launch(intentSenderRequest)
    }
}