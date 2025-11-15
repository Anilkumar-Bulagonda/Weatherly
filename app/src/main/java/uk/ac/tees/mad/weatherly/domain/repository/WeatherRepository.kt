package uk.ac.tees.mad.weatherly.domain.repository

import uk.ac.tees.mad.weatherly.domain.model.WeatherData

interface WeatherRepository {

    suspend fun getWeather(city: String, apiKey: String): WeatherData

}