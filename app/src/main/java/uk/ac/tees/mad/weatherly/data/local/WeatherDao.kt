package uk.ac.tees.mad.weatherly.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {


    @Query("""
    SELECT * FROM weather_table
    WHERE REPLACE(LOWER(cityName), ' ', '') LIKE '%' || REPLACE(LOWER(:cityName), ' ', '') || '%'
    LIMIT 1
""")
    fun getWeatherByCity(cityName: String): Flow<WeatherEntity?>



    @Upsert
    suspend fun upsertWeather(weather: WeatherEntity)
}