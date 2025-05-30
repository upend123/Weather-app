package com.example.weatherapp.data.remote.api

import com.example.weatherapp.data.remote.dto.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("key")
        key: String,
        @Query("q")
        city : String
    ): Response<WeatherModel>


}