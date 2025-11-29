package uk.ac.tees.mad.weatherly.data.local.forcast

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ForecastConverters {

    @TypeConverter
    fun fromForecastList(list: List<EntityForecastData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<EntityForecastData>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toForecastList(value: String): List<EntityForecastData> {
        val gson = Gson()
        val type = object : TypeToken<List<EntityForecastData>>() {}.type
        return gson.fromJson(value, type)
    }
}