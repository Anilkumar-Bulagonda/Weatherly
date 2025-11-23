package uk.ac.tees.mad.weatherly.presentaion.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.weatherly.data.local.WeatherDao
import uk.ac.tees.mad.weatherly.data.local.WeatherEntity
import uk.ac.tees.mad.weatherly.data.remote.supabase.SupabaseClientProvider
import uk.ac.tees.mad.weatherly.domain.model.DomainAqiData
import uk.ac.tees.mad.weatherly.domain.model.DomainHourlyData
import uk.ac.tees.mad.weatherly.domain.model.DomainWeatherData
import uk.ac.tees.mad.weatherly.domain.repository.NetworkConnectivityObserver
import uk.ac.tees.mad.weatherly.domain.repository.WeatherRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val weatherDao: WeatherDao,
    private val connectivityObserver: NetworkConnectivityObserver,

    ) : ViewModel() {


//    https://api.openweathermap.org/geo/1.0/direct?q=London&limit=1&appid=YOUR_API_KEY


    val status = connectivityObserver.networkStatus
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _query = MutableStateFlow("")

    private val _Domain_weatherData = MutableStateFlow<DomainWeatherData?>(null)
    val weatherData = _Domain_weatherData.asStateFlow()

    private val _aqiData = MutableStateFlow<DomainAqiData?>(null)
    val aqiData = _aqiData.asStateFlow()

    private val _localWeatherData = MutableStateFlow<WeatherEntity?>(null)
    val localWeatherDat = _localWeatherData.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()


    private val _hourlyWeather = MutableStateFlow<List<DomainHourlyData>>(emptyList())
    val hourlyWeather: StateFlow<List<DomainHourlyData>> = _hourlyWeather
    private val _isLoading = MutableStateFlow(false)
    var isLoading: StateFlow<Boolean> = _isLoading
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun updateQuery(q: String) {
        _query.update {

            q

        }
    }

    init {
        viewModelScope.launch {
            _query.filter { it.isNotBlank() }.distinctUntilChanged().debounce(100)
                .collectLatest { query ->

                    val data = weatherRepository.getWeather(
                        city = query, apiKey = "2918d47481d7d0abd2195b35a3f64a1c"
                    )

                    val aqiData = weatherRepository.getAqi(
                        lat = data.latitude,
                        lon = data.latitude,
                        apiKey = "2918d47481d7d0abd2195b35a3f64a1c"
                    )

                    _aqiData.value = aqiData


                    Log.d("HomeViewModel", "WeatherData updated: $data")

                    _Domain_weatherData.value = data

                    if (data.condition == "Unknown") {

                    } else {
                        weatherDao.upsertWeather(
                            WeatherEntity(
                                cityName = data.cityName,
                                latitude = data.latitude,
                                longitude = data.longitude,
                                temperature = data.temperature,
                                condition = data.condition,
                                humidity = data.humidity,
                                pressure = data.pressure,
                                airQualityIndex = data.airQualityIndex,
                                temp_max = data.temp_max,
                                temp_min = data.temp_min,
                                icon = data.icon
                            )
                        )
                    }

                    fetchForecastData(
                        city = query, apiKey = "2918d47481d7d0abd2195b35a3f64a1c"
                    )


                    weatherDao.getWeatherByCity(query).collect { localData ->
                        _localWeatherData.value = localData
                    }


                }
        }


    }


    fun fetchForecastData(city: String, apiKey: String) {
        viewModelScope.launch {

            _error.value = null
            try {
                val list = weatherRepository.getHourlyWeather(city, apiKey)
                _hourlyWeather.value = list
            } catch (e: Exception) {
                _error.value = "Failed to fetch hourly weather"
            } finally {

            }
        }
    }


    private val _lickedCity = MutableStateFlow<List<WeatherEntity>>(emptyList())
    val lickedCity: StateFlow<List<WeatherEntity>> = _lickedCity

    fun getLickedCity() {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {

            _isLoading.value = true

            val snapshot = firestore.collection("user").document(uid).get().await()

            val lickedCity = snapshot.get("lickedCity") as? List<String> ?: emptyList()

            if (lickedCity.isNotEmpty()) {

                lickedCity.forEach { city ->

                    val updatedCityData = weatherRepository.getWeather(
                        city = city,
                        apiKey = "2918d47481d7d0abd2195b35a3f64a1c"
                    )


                    weatherDao.getLickedCity(lickedCity).collect { cityData ->
                        _lickedCity.value = cityData

                    }



                    weatherDao.upsertWeather(
                        WeatherEntity(
                            cityName = updatedCityData.cityName,
                            latitude = updatedCityData.latitude,
                            longitude = updatedCityData.longitude,
                            temperature = updatedCityData.temperature,
                            condition = updatedCityData.condition,
                            humidity = updatedCityData.humidity,
                            pressure = updatedCityData.pressure,
                            airQualityIndex = updatedCityData.airQualityIndex,
                            temp_max = updatedCityData.temp_max,
                            temp_min = updatedCityData.temp_min,
                            icon = updatedCityData.icon
                        )
                    )

                    _isLoading.value = false

                }



            } else {
                _isLoading.value = false
                _lickedCity.value = emptyList()
            }

        }
    }





}