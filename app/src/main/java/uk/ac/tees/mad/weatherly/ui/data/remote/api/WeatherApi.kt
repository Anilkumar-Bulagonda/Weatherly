package uk.ac.tees.mad.weatherly.ui.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.weatherly.ui.data.remote.Dto.SearchLocationDto

//    abstract val city: Unit
interface WeatherApi {


    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
    ): SearchLocationDto


}