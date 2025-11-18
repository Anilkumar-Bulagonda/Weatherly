package uk.ac.tees.mad.weatherly.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.weatherly.data.remote.Dto.AirQualityResponse


interface AirPollutionApi {

    @GET("air_pollution")
    suspend fun getAirQuality(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): AirQualityResponse
}
