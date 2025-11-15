package uk.ac.tees.mad.weatherly.presentaion.utilsScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.weatherly.presentaion.HomeScreen.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {


    var searchQuery by remember { mutableStateOf("") }
    val foucusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var isSugsetionVisible by remember { mutableStateOf(false) }


    val focusRequester = remember { FocusRequester() }


    val weatherData by homeViewModel.weatherData.collectAsStateWithLifecycle()

    var isRefreshing by remember { mutableStateOf(false) }

    val refreshState = rememberPullToRefreshState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                isRefreshing = isRefreshing,
                state = refreshState,
                onRefresh = {
                    isRefreshing = true
                    homeViewModel.updateQuery(searchQuery)
                    isRefreshing  = false
                }
            )
            .padding(10.dp)) {
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

            weatherData?.let { data ->
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "City: ${data.cityName}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Temperature: ${data.temperature}°C")
                Text(text = "Condition: ${data.condition}")
                Text(text = "Humidity: ${data.humidity}%")
                Text(text = "Pressure: ${data.pressure} hPa")
            }


        }


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