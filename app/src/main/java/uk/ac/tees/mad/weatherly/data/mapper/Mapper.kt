package uk.ac.tees.mad.weatherly.data.mapper


import uk.ac.tees.mad.weatherly.data.remote.Dto.WeatherRes
import uk.ac.tees.mad.weatherly.domain.model.DomainWeatherData

fun WeatherRes.toWeatherData(): DomainWeatherData {
    return DomainWeatherData(
        cityName = this.name,
        latitude = this.coord?.lat ?: 0.0,
        longitude = this.coord?.lon ?: 0.0,
        temperature = this.main.temp,
        condition = this.weather.firstOrNull()?.description ?: "Unknown",
        humidity = this.main.humidity,
        pressure = this.main.pressure,
        airQualityIndex = -1,
        temp_min = this.main.temp_min,
        temp_max = this.main.temp_max,
        icon = this.weather.firstOrNull()?.icon ?: ""
    )
}
