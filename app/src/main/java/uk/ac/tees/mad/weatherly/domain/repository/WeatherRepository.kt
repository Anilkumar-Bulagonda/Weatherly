package uk.ac.tees.mad.weatherly.domain.repository

import uk.ac.tees.mad.weatherly.domain.model.DomainHourlyData
import uk.ac.tees.mad.weatherly.domain.model.DomainWeatherData

interface WeatherRepository {

    suspend fun getWeather(city: String, apiKey: String): DomainWeatherData

    suspend fun getHourlyWeather(city: String, apiKey: String): List<DomainHourlyData>

}