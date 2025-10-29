package uk.ac.tees.mad.weatherly.ui.presentaion.navigation

sealed class Routes {


    @kotlinx.serialization.Serializable
    data object AuthScreen

    @kotlinx.serialization.Serializable
    data object SingInScreen

    @kotlinx.serialization.Serializable
    data object LogInScreen

    @kotlinx.serialization.Serializable
    data object HomeScreen



}