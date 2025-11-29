package uk.ac.tees.mad.weatherly.data.mapper

import uk.ac.tees.mad.weatherly.data.local.forcast.EntityForecastData
import uk.ac.tees.mad.weatherly.domain.model.DomainForecastData

fun List<DomainForecastData>.toEntityList(): List<EntityForecastData> {
    return this.map { domain ->
        EntityForecastData(
            time = domain.time,
            temperature = domain.temperature,
            condition = domain.condition,
            description = domain.description,
            icon = domain.icon
        )
    }
}
