package uk.ac.tees.mad.weatherly.data.mapper


import uk.ac.tees.mad.weatherly.data.remote.api.AirQualityResponse
import uk.ac.tees.mad.weatherly.domain.model.DomainAqiData

fun AirQualityResponse.toDomainAqiData(): DomainAqiData {

    val aqiValue = list.firstOrNull()?.main?.aqi ?: 0
    return DomainAqiData(aqi = aqiValue)
}
