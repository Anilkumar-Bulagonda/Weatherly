package uk.ac.tees.mad.weatherly.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.weatherly.data.remote.Dto.WeatherResponse

//    abstract val city: Unit
interface WeatherApi {


    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
    ): WeatherResponse


}