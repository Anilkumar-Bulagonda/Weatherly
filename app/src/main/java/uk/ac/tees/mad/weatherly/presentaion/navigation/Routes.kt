package uk.ac.tees.mad.weatherly.presentaion.navigation

import kotlinx.serialization.Serializable

sealed class Routes {


    @Serializable
    data object AuthScreen

    @Serializable
    data object SingInScreen

    @Serializable
    data object LogInScreen

    @Serializable
    data object HomeScreen



}