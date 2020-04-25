package com.mandriv.ctnotifications.contextproviders.weather

import com.mandriv.ctnotifications.contextproviders.weather.pojos.WeatherDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query(value = "lat") lat: Double,
        @Query(value = "lon") long: Double,
        @Query(value = "units") unit: String,
        @Query(value = "appid") apiKey: String
    ): WeatherDataResponse
}