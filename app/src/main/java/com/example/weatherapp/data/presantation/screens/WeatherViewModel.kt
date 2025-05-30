package com.example.weatherapp.data.presantation.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.NetworkResponse
import com.example.weatherapp.data.remote.api.RetrofitInstance
import com.example.weatherapp.data.remote.dto.WeatherModel
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance().weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult


    fun getData(city : String){
        Log.d("WeatherViewModel", "Fetching weather for city: $city")
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response: Response<WeatherModel> = weatherApi.getWeather(BuildConfig.WEATHER_API_KEY,city)
                if(response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                    Log.d("Response:", response.body().toString())
                }else{
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                    Log.d("Error :", response.message())
                }
            }
            catch (e: Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load data")
            }

        }

    }


}