package uk.ac.tees.mad.weatherly.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.weatherly.data.local.forcast.ForecastEntity
import uk.ac.tees.mad.weatherly.data.local.weather.WeatherEntity

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



    @Query("SELECT * FROM weather_table WHERE cityName IN (:city)")
    fun getLickedCity(city: List<String>): Flow<List<WeatherEntity>>

    @Upsert
    suspend fun updateLikedCities(cities: List<WeatherEntity>)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: ForecastEntity)

    @Query("""
    SELECT * FROM forecast_table
    WHERE REPLACE(LOWER(cityName), ' ', '') LIKE '%' || REPLACE(LOWER(:city), ' ', '') || '%'
    LIMIT 1
""")
    fun getForecast(city: String): Flow<ForecastEntity?>


}