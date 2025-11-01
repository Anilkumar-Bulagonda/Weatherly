package uk.ac.tees.mad.weatherly.presentaion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.weatherly.presentaion.AuthScreens.AuthScreen
import uk.ac.tees.mad.weatherly.presentaion.AuthScreens.LoginScreen


import uk.ac.tees.mad.weatherly.presentaion.AuthScreens.SignUpScreen

import uk.ac.tees.mad.weatherly.presentaion.HomeScreen.HomeScreen
import uk.ac.tees.mad.weatherly.presentaion.HomeScreen.HomeViewModel

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
) {


    val auth = FirebaseAuth.getInstance()


    var currentUser by remember { mutableStateOf(auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener {
            currentUser = it.currentUser
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    val startDestination = if (currentUser == null) {
        Routes.AuthScreen
    } else {
        Routes.HomeScreen
    }

    NavHost(navController, startDestination = startDestination) {


        composable<Routes.AuthScreen> {


            AuthScreen(
                navController = navController,
            )

        }

        composable<Routes.SingInScreen> {


            SignUpScreen(
                authViewModel = authViewModel,
                navController = navController
            )

        }

        composable<Routes.HomeScreen> {


            HomeScreen(

                homeViewModel = homeViewModel,
                authViewModel = authViewModel,
                navController = navController
            )

        }

        composable<Routes.LogInScreen> {


            LoginScreen(
                authViewModel = authViewModel,
                navController = navController
            )

        }

    }


}