package uk.ac.tees.mad.weatherly.data.remote.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query


interface AirPollutionApi {
    @GET("air_pollution")
    suspend fun getAirQuality(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
    ): AirQualityResponse
}

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
