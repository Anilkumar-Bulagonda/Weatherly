package uk.ac.tees.mad.weatherly.data.local.forcast

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.weatherly.domain.model.DomainForecastData

@Entity(tableName = "forecast_table")
data class ForecastEntity (

    @PrimaryKey
    val cityName : String,
    val DaysData: List<DomainForecastData>

)



