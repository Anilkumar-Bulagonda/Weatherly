package uk.ac.tees.mad.weatherly.di

import dagger.Module
import dagger.Provides

import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.weatherly.ui.data.remote.api.WeatherApi
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
    fun provideRetrofit( @Named("base_url") BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("WeatherApi")
    fun provideWeatherApi( @Named("Retrofit") retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)



}


