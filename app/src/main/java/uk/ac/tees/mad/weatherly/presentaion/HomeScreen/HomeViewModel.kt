package uk.ac.tees.mad.weatherly.presentaion.HomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.weatherly.domain.model.WeatherData
import uk.ac.tees.mad.weatherly.domain.repository.WeatherRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val _query = MutableStateFlow("")

    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData = _weatherData.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun updateQuery(q: String) {
        _query.update {

            q

        }
    }

    init {
        viewModelScope.launch {
            _query
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .debounce(500)
                .collectLatest { query ->
                    val data = weatherRepository.getWeather(
                        city = query,
                        apiKey = "2918d47481d7d0abd2195b35a3f64a1c"
                    )


                    Log.d("HomeViewModel", "WeatherData updated: $data")

                    _weatherData.value = data
                }
        }
    }


}