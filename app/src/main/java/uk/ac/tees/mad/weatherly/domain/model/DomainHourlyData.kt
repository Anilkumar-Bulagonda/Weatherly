package uk.ac.tees.mad.weatherly.domain.model



data class DomainHourlyData(
    val time: String,          // Forecast time
    val temperature: Double,   // Temperature in °C
    val condition: String,     // Main condition, e.g., "Clear"
    val description: String,   // Description, e.g., "clear sky"
    val icon: String           // Icon code, e.g., "01d"
)
