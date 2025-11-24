package uk.ac.tees.mad.weatherly.domain.model



data class DomainForecastData(
    val time: String,
    val temperature: Double,
    val condition: String,
    val description: String,
    val icon: String
)
