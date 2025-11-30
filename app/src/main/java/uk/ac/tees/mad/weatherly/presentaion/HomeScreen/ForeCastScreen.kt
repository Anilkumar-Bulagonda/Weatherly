package uk.ac.tees.mad.weatherly.presentaion.HomeScreen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import uk.ac.tees.mad.weatherly.R
import uk.ac.tees.mad.weatherly.data.local.forcast.EntityForecastData
import uk.ac.tees.mad.weatherly.presentaion.viewModels.HomeViewModel


@Composable
fun ForecastChartUI(city: String, homeViewModel: HomeViewModel) {


    LaunchedEffect(Unit) {
        homeViewModel.getF(city)
    }

    val forecastEntity = homeViewModel.forecastDomainData.collectAsState().value

    Scaffold( modifier = Modifier.fillMaxSize(),
        containerColor = colorResource(id = R.color.white),


    ) { innerPadding ->

        val context = LocalContext.current
        val activity = context as? Activity

        DisposableEffect(Unit) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            onDispose {

                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }

            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {


                forecastEntity?.let {
                    ForecastChart(
                        forecastList = it.DaysData, modifier = Modifier.fillMaxWidth(),
                        city = city
                    )
                } ?: run {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Loading forecast...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
            }


    }

}




@Composable
fun ForecastChart(
    forecastList: List<EntityForecastData>,
    modifier: Modifier = Modifier,
    city: String
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "$city Forecast",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color(0xFF6AC1FA),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 1.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        AndroidView(
            factory = { ctx ->
                LineChart(ctx).apply {
                    description.isEnabled = false
                    setTouchEnabled(true)
                    setPinchZoom(true)
                    setDrawGridBackground(false)
                    setBackgroundColor(android.graphics.Color.WHITE)

                    val entries = forecastList.mapIndexed { index, forecast ->
                        Entry(index.toFloat(), forecast.temperature.toFloat())
                    }

                    val dataSet = LineDataSet(entries, "Temperature (°C)").apply {
                        color = android.graphics.Color.parseColor("#2196F3")
                        setCircleColor(android.graphics.Color.parseColor("#64B5F6"))
                        lineWidth = 2.5f
                        circleRadius = 4f
                        setDrawCircleHole(false)
                        valueTextColor = android.graphics.Color.parseColor("#0D47A1")
                        valueTextSize = 10f
                        mode = LineDataSet.Mode.CUBIC_BEZIER
                        setDrawValues(true)
                        valueFormatter = object : ValueFormatter() {
                            override fun getPointLabel(entry: Entry?): String {
                                return "${entry?.y?.toInt()}°C"
                            }
                        }
                    }

                    data = LineData(dataSet)

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        textColor = android.graphics.Color.parseColor("#0D47A1")
                        granularity = 1f
                        valueFormatter = IndexAxisValueFormatter(
                            forecastList.map { it.time })
                    }

                    axisLeft.apply {
                        textColor = android.graphics.Color.parseColor("#0D47A1")
                        setDrawGridLines(true)
                        gridColor = android.graphics.Color.parseColor("#BBDEFB")
                        valueFormatter = object : ValueFormatter() {
                            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                                return "${value.toInt()}°C"
                            }
                        }
                    }

                    axisRight.isEnabled = false
                    legend.textColor = android.graphics.Color.parseColor("#0D47A1")
                    invalidate()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(8.dp)
        )
    }
}






