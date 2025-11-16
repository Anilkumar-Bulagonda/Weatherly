package uk.ac.tees.mad.weatherly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherEntity::class], version = 2)

abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao


}