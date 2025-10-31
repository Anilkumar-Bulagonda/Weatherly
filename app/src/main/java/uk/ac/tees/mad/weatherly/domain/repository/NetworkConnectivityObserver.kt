package uk.ac.tees.mad.weatherly.domain.repository


import kotlinx.coroutines.flow.StateFlow


interface NetworkConnectivityObserver {


    val networkStatus: StateFlow<NetworkStatus>


}

sealed class NetworkStatus {
    data object Connected: NetworkStatus()
    data object Disconnected: NetworkStatus()
}