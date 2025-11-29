package uk.ac.tees.mad.weatherly.data.local.forcast

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_table")
data class ForecastEntity (

    @PrimaryKey
    val cityName : String,
    val DaysData: List<EntityForecastData>

)

data class EntityForecastData (
    val time: String,
    val temperature: Double,
    val condition: String,
    val description: String,
    val icon: String
)

