package uk.ac.tees.mad.weatherly.data.local.forcast

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uk.ac.tees.mad.weatherly.domain.model.DomainForecastData

class ForecastConverters {

    @TypeConverter
    fun fromForecastList(list: List<DomainForecastData>): String {
        val gson = Gson()
        val type = object : TypeToken<List<DomainForecastData>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toForecastList(value: String): List<DomainForecastData> {
        val gson = Gson()
        val type = object : TypeToken<List<DomainForecastData>>() {}.type
        return gson.fromJson(value, type)
    }
}