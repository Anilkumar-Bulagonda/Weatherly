package uk.ac.tees.mad.weatherly.domain.model

data class WeatherData(
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val pressure: Int,
    val airQualityIndex: Int
)
