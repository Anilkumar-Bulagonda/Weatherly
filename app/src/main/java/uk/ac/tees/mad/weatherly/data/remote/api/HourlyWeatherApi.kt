package uk.ac.tees.mad.weatherly.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.weatherly.data.remote.dto.weatherhourly.HourlyWeatherData

interface HourlyWeatherApi {


    @GET("forecast")
    suspend fun getHourlyForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
    ): HourlyWeatherData


}