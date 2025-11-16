//package uk.ac.tees.mad.weatherly.presentaion
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Air
//import androidx.compose.material.icons.filled.Compress
//import androidx.compose.material.icons.filled.Help
//import androidx.compose.material.icons.filled.WaterDrop
//import androidx.compose.material.icons.filled.WbCloudy
//import androidx.compose.material.icons.filled.WbSunny
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import uk.ac.tees.mad.weatherly.domain.model.HourlyWeatherData
//import uk.ac.tees.mad.weatherly.domain.model.WeatherData
//
//// Sample data for demonstration (replace with your actual data)
//val sampleWeatherData = WeatherData(
//    cityName = "New York",
//    latitude = 40.7128,
//    longitude = -74.0060,
//    temperature = 22.5,
//    condition = "Clear",
//    humidity = 65,
//    pressure = 1013,
//    airQualityIndex = 45
//)
//
//val sampleHourlyData = listOf(
//    HourlyWeatherData("12:00", 23.0, "Clear", "clear sky", "01d"),
//    HourlyWeatherData("13:00", 24.5, "Partly Cloudy", "few clouds", "02d"),
//    HourlyWeatherData("14:00", 25.0, "Cloudy", "scattered clouds", "03d"),
//    HourlyWeatherData("15:00", 24.0, "Rain", "light rain", "10d"),
//    HourlyWeatherData("16:00", 22.5, "Clear", "clear sky", "01d")
//)
//
//@Composable
//fun WeatherScreen(
//    weatherData: WeatherData,
//    hourlyForecast: List<HourlyWeatherData>
//) {
//    val gradientColors = listOf(
//        Color(0xFF87CEEB), // Sky Blue
//        Color(0xFF98D8E8)  // Light Blue
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(gradientColors)
//            )
//            .padding(16.dp)
//    ) {
//        // Current Weather Header
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//        ) {
//            Column(
//                modifier = Modifier.padding(24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = weatherData.cityName,
//                    style = MaterialTheme.typography.headlineMedium,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//
//                // Temperature with Icon (using default sunny icon for now)
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.WbSunny,
//                        contentDescription = weatherData.condition,
//                        modifier = Modifier.size(48.dp),
//                        tint = Color(0xFFFFD700) // Golden yellow for sun
//                    )
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Text(
//                        text = "${weatherData.temperature}°C",
//                        style = MaterialTheme.typography.headlineLarge,
//                        fontWeight = FontWeight.ExtraBold,
//                        fontSize = 48.sp
//                    )
//                }
//
//                Text(
//                    text = weatherData.condition,
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color.Gray
//                )
//            }
//        }
//
//        // Weather Details
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp),
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(
//                    text = "Details",
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    WeatherDetailItem(
//                        icon = Icons.Default.WaterDrop,
//                        label = "Humidity",
//                        value = "${weatherData.humidity}%"
//                    )
//                    WeatherDetailItem(
//                        icon = Icons.Default.Compress,
//                        label = "Pressure",
//                        value = "${weatherData.pressure} hPa"
//                    )
//                    WeatherDetailItem(
//                        icon = Icons.Default.Air,
//                        label = "AQI",
//                        value = weatherData.airQualityIndex.toString()
//                    )
//                }
//            }
//        }
//
//        // Hourly Forecast
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.4f), // Adjust as needed
//            shape = RoundedCornerShape(16.dp),
//            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(
//                    text = "Hourly Forecast",
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                LazyRow(
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(hourlyForecast) { hourly ->
//                        HourlyForecastItem(hourly)
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun WeatherDetailItem(
//    icon: ImageVector,
//    label: String,
//    value: String
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.padding(8.dp)
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = label,
//            modifier = Modifier.size(24.dp),
//            tint = Color.Gray
//        )
//        Spacer(modifier = Modifier.height(4.dp))
//        Text(
//            text = value,
//            style = MaterialTheme.typography.bodyLarge,
//            fontWeight = FontWeight.Medium
//        )
//        Text(
//            text = label,
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.Gray,
//            textAlign = TextAlign.Center
//        )
//    }
//}
//
//@Composable
//fun HourlyForecastItem(hourly: HourlyWeatherData) {
//    Card(
//        modifier = Modifier
//            .width(80.dp)
//            .padding(vertical = 4.dp),
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Time
//            Text(
//                text = hourly.time,
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Medium
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Icon (using default icons based on condition for now; map icon string to real icons later)
//            val iconVector = when (hourly.icon) {
//                "01d" -> Icons.Default.WbSunny
//                "02d", "03d" -> Icons.Default.WbCloudy
//                "10d" -> Icons.Default.WaterDrop
//                else -> Icons.Default.Help // Default placeholder
//            }
//            Icon(
//                imageVector = iconVector,
//                contentDescription = hourly.condition,
//                modifier = Modifier.size(24.dp),
//                tint = Color.Gray
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Temperature
//            Text(
//                text = "${hourly.temperature}°",
//                style = MaterialTheme.typography.bodyLarge,
//                fontWeight = FontWeight.Bold
//            )
//
//            // Condition (abbreviated)
//            Text(
//                text = hourly.condition.take(8), // Shorten for space
//                style = MaterialTheme.typography.bodySmall,
//                color = Color.Gray,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//// Preview function (for Android Studio preview)
//@Preview(showBackground = true)
//@Composable
//fun WeatherScreenPreview() {
//    MaterialTheme {
//        WeatherScreen(sampleWeatherData, sampleHourlyData)
//    }
//}
