package uk.ac.tees.mad.weatherly.presentaion.utilsScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier) {


    var searchQuery by remember { mutableStateOf("") }
    val foucusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    var isSugsetionVisible by remember { mutableStateOf(false) }


    val focusRequester = remember { FocusRequester() }



        Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
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
                    onQueryChange = { query = it },
                    onSearch = {



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









//


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