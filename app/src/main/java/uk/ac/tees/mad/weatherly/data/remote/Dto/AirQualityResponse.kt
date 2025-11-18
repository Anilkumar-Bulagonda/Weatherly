package uk.ac.tees.mad.weatherly.data.remote.Dto

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("list")
    val list: List<AirQualityData>
)

data class AirQualityData(
    @SerializedName("main")
    val main: AirQualityMain
)

data class AirQualityMain(
    @SerializedName("aqi")
    val aqi: Int
)
