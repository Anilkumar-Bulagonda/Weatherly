package uk.ac.tees.mad.weatherly.presentaion

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.weatherly.domain.repository.NetworkConnectivityObserver
import uk.ac.tees.mad.weatherly.domain.repository.NetworkStatus
import uk.ac.tees.mad.weatherly.presentaion.navigation.Navigation
import uk.ac.tees.mad.weatherly.presentaion.utilsScreens.NetworkStatusBar
import uk.ac.tees.mad.weatherly.presentaion.viewModels.HomeViewModel
import uk.ac.tees.mad.weatherly.theme.WeatherlyTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityObserver: NetworkConnectivityObserver
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

//        2918d47481d7d0abd2195b35a3f64a1c

        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()


        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this as Activity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        101
                    )
                }
            }


            val status by connectivityObserver.networkStatus.collectAsState()
            val navController: NavHostController = rememberNavController()
            var message by rememberSaveable { mutableStateOf("") }
            var bgColors by remember  { mutableStateOf(Color.Red) }
            var showStatusBar by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = status) {
                when (status) {


                    NetworkStatus.Connected -> {
                        message = "Connected To Internet"
                        bgColors = Color.Green
                        delay(2000)
                        showStatusBar = false
                    }


                    NetworkStatus.Disconnected -> {

                        showStatusBar = true
                        message = "No Internet Connected !!"
                        bgColors = Color.Red

                    }
                }
            }



            WeatherlyTheme {
                Scaffold(
                    modifier = Modifier.Companion.fillMaxSize(),


                    bottomBar = {
                        NetworkStatusBar(
                            showMessageBar = showStatusBar,
                            message = message,
                            backgroundColor = bgColors
                        )
                    }) { innerPadding ->
                    Navigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        authViewModel =authViewModel,
                        homeViewModel = homeViewModel,
                    )

                }
            }
        }
    }
}