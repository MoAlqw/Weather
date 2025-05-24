package com.example.dagger2.model.weather.retrofit

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: Current
)

data class Location(
    @SerializedName("name") val city: String
)

data class Current(
    @SerializedName("temp_c") val tempC: Float,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("wind_kph") val wind: Float
)

data class Condition(
    @SerializedName("text") val description: String,
    @SerializedName("icon") val icon: String
)