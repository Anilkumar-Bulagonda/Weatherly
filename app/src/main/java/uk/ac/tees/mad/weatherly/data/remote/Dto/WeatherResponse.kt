package uk.ac.tees.mad.weatherly.data.remote.Dto

data class WeatherResponse(
    val name: String, // city
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double,
    val feels_like: Double
)

data class Weather(
    val description: String
)
