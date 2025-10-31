package uk.ac.tees.mad.weatherly.di

import android.content.Context
import dagger.Module
import dagger.Provides

import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.weatherly.data.remote.api.WeatherApi
import uk.ac.tees.mad.weatherly.data.repositroyIMPL.NetworkConnectivityObserverImpl
import uk.ac.tees.mad.weatherly.domain.repository.NetworkConnectivityObserver
import javax.inject.Named
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object Module {


    @Provides
    @Named("base_url")
    fun provideBaseUrl() = "https://api.openweathermap.org/data/2.5/"

    @Provides
    @Singleton
    @Named("Retrofit")
    fun provideRetrofit(@Named("base_url") BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("WeatherApi")
    fun provideWeatherApi(@Named("Retrofit") retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)


    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(kotlinx.coroutines.Dispatchers.IO)

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context,
                                           scope: CoroutineScope): NetworkConnectivityObserver
    {
        return NetworkConnectivityObserverImpl(context, scope)
    }


}


