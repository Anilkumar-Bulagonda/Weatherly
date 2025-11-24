package uk.ac.tees.mad.weatherly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.tees.mad.weatherly.data.local.forcast.ForecastConverters
import uk.ac.tees.mad.weatherly.data.local.forcast.ForecastEntity
import uk.ac.tees.mad.weatherly.data.local.weather.WeatherEntity

@Database(entities = [WeatherEntity::class, ForecastEntity::class] , version = 3)
@TypeConverters(ForecastConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao


}