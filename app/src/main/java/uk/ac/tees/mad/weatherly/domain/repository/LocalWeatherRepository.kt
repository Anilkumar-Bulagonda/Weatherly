package uk.ac.tees.mad.weatherly.domain.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.weatherly.data.local.weather.WeatherEntity

interface LocalWeatherRepository {


    fun getWeatherByCity(cityName: String): Flow<WeatherEntity>


    suspend fun upsertWeather(weather: WeatherEntity)




}