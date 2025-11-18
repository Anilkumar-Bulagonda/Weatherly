package uk.ac.tees.mad.weatherly.presentaion.utilsScreens

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.FilterDrama
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.compose.ui.platform.LocalContext
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
import uk.ac.tees.mad.weatherly.domain.model.DomainWeatherData
import uk.ac.tees.mad.weatherly.domain.repository.NetworkStatus
import uk.ac.tees.mad.weatherly.presentaion.viewModels.HomeViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {

    val localWeatherData by homeViewModel.localWeatherDat.collectAsStateWithLifecycle()
    val hourlyWeather by homeViewModel.hourlyWeather.collectAsState()
    val aqiData by homeViewModel.aqiData.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    val refreshState = rememberPullToRefreshState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val currentTime = LocalTime.now()


    val foucusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSugsetionVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val gradientColors = listOf(
        Color(0xFF87CEEB),
        Color(0xFF98D8E8)
    )

//    Network
 val context = LocalContext.current

    val netWorkState = homeViewModel.status.collectAsState()
    val isNetworkAvailable = homeViewModel.status.collectAsState().value == NetworkStatus.Connected




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


                        if (!isNetworkAvailable) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                                        context.startActivity(intent)
                                    }
                                    .padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WifiOff,
                                    contentDescription = "No Internet",
                                    tint = Color.Red.copy(alpha = 0.8f),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Connect to Internet",
                                    color = Color.Red.copy(alpha = 0.9f),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }

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
                        val (icon, tint) = when(weatherData.icon) {
                            "01d", "01n" -> Icons.Default.WbSunny to Color(0xFFFFD700)
                            "02d", "02n" -> Icons.Default.CloudQueue to Color.Gray
                            "03d", "03n", "04d", "04n" -> Icons.Default.Cloud to Color.Gray
                            "09d", "09n", "10d", "10n" -> Icons.Default.Grain to Color(0xFF2196F3)
                            "11d", "11n" -> Icons.Default.FlashOn to Color(0xFFFF5722)
                            "13d", "13n" -> Icons.Default.AcUnit to Color(0xFF00BCD4)
                            "50d", "50n" -> Icons.Default.FilterDrama to Color.Gray
                            else -> Icons.Default.HelpOutline to Color.Black
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = "Weather Icon",
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.CenterHorizontally),
                            tint = tint
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


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Details",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp), color = Color.Black
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            WeatherDetailItem(
                                icon = Icons.Default.WaterDrop,
                                label = "Humidity",
                                value = "${weatherData.humidity}%"
                            )
                            WeatherDetailItem(
                                icon = Icons.Default.Compress,
                                label = "Pressure",
                                value = "${weatherData.pressure} hPa"
                            )
                            WeatherDetailItem(
                                icon = Icons.Default.Air,
                                label = "AQI",
                                value = aqiData?.aqi.toString()
                            )
                        }
                    }
                }


                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(hourlyWeather) { index, hourly ->

                        val formatter = DateTimeFormatter.ofPattern("h a")
                        val time = currentTime.plusHours(index.toLong())
                        val formattedTime = time.format(formatter)
                        HourlyForecastItem(hourly,time = formattedTime)
                    }
                }


            } ?: run {
                val sampleWeatherData = DomainWeatherData(
                    cityName = "Search For City ",
                    latitude = 0.000,
                    longitude = -0.000,
                    temperature = 00.0,
                    temp_min = 00.0,
                    temp_max = 00.0,
                    condition = "Unknown",
                    humidity = 0,
                    pressure = 0,
                    airQualityIndex = 0,
                    icon = "",
                )
                val sampleHourlyData = listOf(
                    DomainHourlyData("12:00", 23.0, "Clear", "clear sky", "01d"),
                    DomainHourlyData("13:00", 24.5, "Partly Cloudy", "few clouds", "02d"),
                    DomainHourlyData("14:00", 25.0, "Cloudy", "scattered clouds", "03d"),
                    DomainHourlyData("15:00", 24.0, "Rain", "light rain", "10d"),
                    DomainHourlyData("16:00", 22.5, "Clear", "clear sky", "01d")
                )
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
                        if (!isNetworkAvailable) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                                        context.startActivity(intent)
                                    }
                                    .padding(vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WifiOff,
                                    contentDescription = "No Internet",
                                    tint = Color.Red.copy(alpha = 0.8f),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Connect to Internet",
                                    color = Color.Red.copy(alpha = 0.9f),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp, end = 16.dp)
                        ) {
                            Text(
                                text = sampleWeatherData.cityName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }


                        Icon(
                            imageVector = Icons.Default.HelpOutline, // question mark
                            contentDescription = sampleWeatherData.condition,
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.CenterHorizontally),
                            tint = Color.Gray // or any color you like
                        )

                        Spacer(modifier = Modifier.height(8.dp))


                        Text(
                            text = "${sampleWeatherData.temperature}°C",
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
                                text = "Min: ${sampleWeatherData.temp_min}°C",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp
                                ),
                                color = Color(0xFF757575)
                            )
                            Text(
                                text = "Max: ${sampleWeatherData.temp_max}°C",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp
                                ),
                                color = Color(0xFF757575)
                            )
                        }


                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = sampleWeatherData.condition,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                            color = Color(0xFF757575),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )


                    }
                }


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Details",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,  // Add this to make the text black
                            modifier = Modifier.padding(bottom = 16.dp),

                            )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            WeatherDetailItem(
                                icon = Icons.Default.WaterDrop,
                                label = "Humidity",
                                value = "${sampleWeatherData.humidity}%"
                            )
                            WeatherDetailItem(
                                icon = Icons.Default.Compress,
                                label = "Pressure",
                                value = "${sampleWeatherData.pressure} hPa"
                            )
                            WeatherDetailItem(
                                icon = Icons.Default.Air,
                                label = "AQI",
                                value = sampleWeatherData.airQualityIndex.toString()
                            )
                        }
                    }
                }


                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(sampleHourlyData) { index , hourly ->

                        val formatter = DateTimeFormatter.ofPattern("h a")
                        val time = currentTime.plusHours(index.toLong())
                        val formattedTime = time.format(formatter)
                        HourlyForecastItem(hourly,time = formattedTime)
                    }
                }

            }


        }


    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecastItem(hourly: DomainHourlyData,time : String ) {
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


            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
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
                tint = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Temperature
            Text(
                text = "${hourly.temperature}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
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
            tint = Color.Black  // Dark icon tint
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Color.Black  // Dark text
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black.copy(alpha = 0.7f),  // Darker gray text for label
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

