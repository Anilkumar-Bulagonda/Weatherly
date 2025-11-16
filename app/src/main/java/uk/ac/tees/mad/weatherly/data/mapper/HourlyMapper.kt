package uk.ac.tees.mad.weatherly.data.mapper

import uk.ac.tees.mad.weatherly.domain.model.DomainHourlyData


fun uk.ac.tees.mad.weatherly.data.remote.dto.weatherhourly.HourlyWeatherData.toHourlyWeatherDomain(): List<DomainHourlyData> {
    return this.hourlyList.map { item ->
        val weather = item.weatherInfo.firstOrNull()
        DomainHourlyData(
            time = item.forecastTime,
            temperature = item.temperatureInfo.temperature,
            condition = weather?.condition ?: "Unknown",
            description = weather?.description ?: "Unknown",
            icon = weather?.iconCode ?: ""
        )
    }
}
