package uk.ac.tees.mad.weatherly.data.repositroyIMPL

import uk.ac.tees.mad.weatherly.data.mapper.toWeatherData
import uk.ac.tees.mad.weatherly.data.remote.api.WeatherApi
import uk.ac.tees.mad.weatherly.domain.model.WeatherData
import uk.ac.tees.mad.weatherly.domain.repository.WeatherRepository

class WeatherRepositoryImpl(val api: WeatherApi) : WeatherRepository {
    override suspend fun getWeather(
        city: String,
        apiKey: String,
    ): WeatherData {


        return try {
            api.getWeatherByCity(city, apiKey).toWeatherData()
        } catch (e: Exception) {
            WeatherData(
                cityName = city,
                latitude = 0.0,
                longitude = 0.0,
                temperature = 0.0,
                condition = "Unknown",
                humidity = 0,
                pressure = 0,
                airQualityIndex = -1
            )
        }


    }


}