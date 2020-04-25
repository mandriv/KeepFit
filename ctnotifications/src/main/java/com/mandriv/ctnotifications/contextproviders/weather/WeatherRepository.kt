package com.mandriv.ctnotifications.contextproviders.weather

import com.mandriv.ctnotifications.contextproviders.weather.pojos.WeatherDataResponse
import com.mandriv.ctnotifications.utils.WEATHER_API_KEY
import com.mandriv.ctnotifications.utils.WEATHER_UNITS


object WeatherRepository {
    var client: WeatherApi = WeatherClient.service

    suspend fun getCurrentWeather(lat: Double, long: Double): WeatherDataResponse {
        return client.getCurrentWeather(lat, long, WEATHER_UNITS, WEATHER_API_KEY)
    }
}