package uk.ac.tees.mad.weatherly.presentaion.utilsScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.weatherly.domain.model.DomainHourlyData
import uk.ac.tees.mad.weatherly.presentaion.viewModels.HomeViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {

    val localWeatherData by homeViewModel.localWeatherDat.collectAsStateWithLifecycle()
    val hourlyWeather by homeViewModel.hourlyWeather.collectAsState()

    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()
    var searchQuery by remember { mutableStateOf("") }


    val foucusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSugsetionVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val gradientColors = listOf(
        Color(0xFF87CEEB),
        Color(0xFF98D8E8)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var query by remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                delay(500)
                focusRequester.requestFocus()

            }

            SearchBar(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged {

                        isSugsetionVisible = it.isFocused
                    },
                query = query,
                onQueryChange = {
                    query = it
                    searchQuery = it
                    homeViewModel.updateQuery(query)
                },
                onSearch = {
                    homeViewModel.updateQuery(query)
                    keyboardController?.hide()
                    foucusManager.clearFocus()

                },
                active = false,
                onActiveChange = {},
                placeholder = { Text("Search") },
                trailingIcon = {

                    IconButton(onClick = {
                        if (query.isNotEmpty()) {
                            query = ""
                            foucusManager.clearFocus()
                        } else {
                            foucusManager.clearFocus()
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(1000)

                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "close")
                    }

                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                }) {


            }
            AnimatedVisibility(visible = isSugsetionVisible) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(searchKeywords) { item: String ->
                        SuggestionChip(
                            onClick = {
                                homeViewModel.updateQuery(item)
                                query = item
                                keyboardController?.hide()
                                foucusManager.clearFocus()

                            },
                            label = { Text(text = item) },
                            modifier = Modifier,
                        )

                    }
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            localWeatherData?.let { weatherData ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp, end = 16.dp)
                        ) {
                            Text(
                                text = weatherData.cityName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }


                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = weatherData.condition,
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.CenterHorizontally),
                            tint = Color(0xFFFFD700)
                        )

                        Spacer(modifier = Modifier.height(8.dp))


                        Text(
                            text = "${weatherData.temperature}°C",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 52.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = Color.Black.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Min: ${weatherData.temp_min}°C",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp
                                ),
                                color = Color(0xFF757575)
                            )
                            Text(
                                text = "Max: ${weatherData.temp_max}°C",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp
                                ),
                                color = Color(0xFF757575)
                            )
                        }


                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = weatherData.condition,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                            color = Color(0xFF757575),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )


                    }
                }


//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text(
//                            text = "Details",
//                            style = MaterialTheme.typography.titleLarge,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceEvenly
//                        ) {
//                            WeatherDetailItem(
//                                icon = Icons.Default.WaterDrop,
//                                label = "Humidity",
//                                value = "${weatherData.humidity}%"
//                            )
//                            WeatherDetailItem(
//                                icon = Icons.Default.Compress,
//                                label = "Pressure",
//                                value = "${weatherData.pressure} hPa"
//                            )
//                            WeatherDetailItem(
//                                icon = Icons.Default.Air,
//                                label = "AQI",
//                                value = weatherData.airQualityIndex.toString()
//                            )
//                        }
//                    }
//                }


                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(hourlyWeather) { hourly ->
                        HourlyForecastItem(hourly)
                    }
                }


            }


        }


    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecastItem(hourly: DomainHourlyData) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Time (single line only)
            val timeOnly = try {
                OffsetDateTime.parse(hourly.time)
                    .toLocalTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
            } catch (e: Exception) {
                "null"
            }

            Text(
                text = timeOnly.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))


            val iconVector = when (hourly.icon) {
                "01d" -> Icons.Default.WbSunny
                "02d", "03d" -> Icons.Default.WbCloudy
                "10d" -> Icons.Default.WaterDrop
                else -> Icons.Default.Help
            }
            Icon(
                imageVector = iconVector,
                contentDescription = hourly.condition,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Temperature
            Text(
                text = "${hourly.temperature}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun WeatherDetailItem(
    icon: ImageVector,
    label: String,
    value: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}




val searchKeywords: List<String> = listOf(
    "London",
    "Birmingham",
    "Manchester",
    "Glasgow",
    "Liverpool",
    "Leeds",
    "Sheffield",
    "Edinburgh",
    "Bristol",
    "Leicester",
    "Nottingham",
    "Cardiff",
    "Newcastle",
    "Belfast",
    "Coventry",
    "Brighton",
    "Southampton",
    "Portsmouth",
    "Aberdeen",
    "Cambridge"
)

//Delhi
//Condition: Clear Sky
//Temperature: 28°C
//Feels Like: 28°C
//Min / Max: 28°C / 28°C
//Humidity: 49%
//Pressure: 1013 hPa
//Wind: 1.0 m/s
//AQI: 78 (Moderate)