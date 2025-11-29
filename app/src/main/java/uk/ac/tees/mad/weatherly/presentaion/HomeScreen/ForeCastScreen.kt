package uk.ac.tees.mad.weatherly.presentaion.HomeScreen

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import uk.ac.tees.mad.weatherly.R
import uk.ac.tees.mad.weatherly.data.local.forcast.EntityForecastData
import uk.ac.tees.mad.weatherly.presentaion.viewModels.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastChartUI(city: String, homeViewModel: HomeViewModel) {


    LaunchedEffect(Unit) {
        homeViewModel.getF(city)

    }


    val forecastEnity = homeViewModel.forecastDomainData.collectAsState().value





    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Forecast",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.app)
                ),

                )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            forecastEnity?.let {

                
                ForecastChart(it.DaysData)


            }

        }


    }

}


@Composable
fun ForecastChart(forecastList: List<EntityForecastData>) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                setDrawGridBackground(false)

                val entries = forecastList.mapIndexed { index, forecast ->
                    Entry(index.toFloat(), forecast.temperature.toFloat())
                }

                val dataSet = LineDataSet(entries, "Temperature (°C)").apply {
                    color = parseColor("#2196F3")
                    setCircleColor(parseColor("#64B5F6"))
                    lineWidth = 2f
                    circleRadius = 4f
                    setDrawCircleHole(false)
                    valueTextColor = parseColor("#0D47A1")
                    valueTextSize = 10f
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                }

                val dataSets = ArrayList<ILineDataSet>()
                dataSets.add(dataSet)
                data = LineData(dataSets)


                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    textColor = parseColor("#0D47A1")
                    granularity = 1f
                    valueFormatter = com.github.mikephil.charting.formatter.IndexAxisValueFormatter(
                        forecastList.map { it.time }
                    )
                }

                axisLeft.apply {
                    textColor = parseColor("#0D47A1")
                    setDrawGridLines(true)
                    gridColor = parseColor("#BBDEFB")
                }

                axisRight.isEnabled = false
                legend.textColor = parseColor("#0D47A1")
                invalidate()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
