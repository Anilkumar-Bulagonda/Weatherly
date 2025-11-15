package uk.ac.tees.mad.weatherly.data.mapper

import uk.ac.tees.mad.weatherly.data.remote.Dto.WeatherResponse.WeatherResponse
import uk.ac.tees.mad.weatherly.domain.model.WeatherData

fun WeatherResponse.toWeatherData(): WeatherData {
    return WeatherData(
        cityName = this.name,
        latitude = this.coord?.lat
            ?: 0.0,
        longitude = this.coord?.lon ?: 0.0,
        temperature = this.main.temp,
        condition = this.weather.firstOrNull()?.description ?: "Unknown",
        humidity = this.main.humidity,
        pressure = this.main.pressure,
        airQualityIndex = -1
    )
}
