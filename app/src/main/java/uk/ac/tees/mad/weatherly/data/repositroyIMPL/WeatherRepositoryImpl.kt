package uk.ac.tees.mad.weatherly.data.repositroyIMPL

import uk.ac.tees.mad.weatherly.data.mapper.toHourlyWeatherDomain
import uk.ac.tees.mad.weatherly.data.mapper.toWeatherData
import uk.ac.tees.mad.weatherly.data.remote.api.HourlyWeatherApi
import uk.ac.tees.mad.weatherly.data.remote.api.WeatherApi
import uk.ac.tees.mad.weatherly.domain.model.DomainHourlyData
import uk.ac.tees.mad.weatherly.domain.model.DomainWeatherData
import uk.ac.tees.mad.weatherly.domain.repository.WeatherRepository

class WeatherRepositoryImpl(val api: WeatherApi, val hourlyApi: HourlyWeatherApi) :
    WeatherRepository {
    override suspend fun getWeather(
        city: String,
        apiKey: String,
    ): DomainWeatherData {


        return try {
            api.getWeatherByCity(city, apiKey).toWeatherData()
        } catch (e: Exception) {
            DomainWeatherData(
                cityName = city,
                latitude = 0.0,
                longitude = 0.0,
                temperature = 0.0,
                condition = "Unknown",
                humidity = 0,
                pressure = 0,
                airQualityIndex = -1,
                temp_max = 0.0,
                temp_min =0.0,
                icon = ""
            )
        }


    }

    override suspend fun getHourlyWeather(
        city: String,
        apiKey: String,
    ): List<DomainHourlyData> {
        return try {

            val dto = hourlyApi.getHourlyForecast(city, apiKey)

            dto.toHourlyWeatherDomain()
        } catch (e: Exception) {
            e.printStackTrace()

            emptyList()
        }
    }


}