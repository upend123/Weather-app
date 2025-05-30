package com.example.weatherapp.data.presantation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.R
import com.example.weatherapp.common.NetworkResponse
import com.example.weatherapp.data.remote.dto.WeatherModel
import com.example.weatherapp.data.presantation.screens.WeatherViewModel


@Composable
fun WeatherPageUI(viewModel: WeatherViewModel = WeatherViewModel()) {
    var city by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val weatherResult = viewModel.weatherResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()) // Add this line
            , // Additional internal padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ☁️ Stylish Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(100.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Cyan, Color(0xFF03A9F4))
                    ),
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Upendra's Weather App",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
         Column(modifier = Modifier
             .fillMaxSize()
             .padding(horizontal = 16.dp)) {

             // 🔍 Search Bar
             Row(
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(bottom = 16.dp),
                 verticalAlignment = Alignment.CenterVertically
             ) {
                 OutlinedTextField(
                     value = city,
                     onValueChange = { city = it },
                     modifier = Modifier
                         .fillMaxWidth()
                         ,
                     colors = OutlinedTextFieldDefaults.colors(
                         focusedBorderColor = Color.Cyan),
                         placeholder = { Text("Enter city name") },
                         shape = RoundedCornerShape(20.dp),
                         singleLine = true,
                         leadingIcon = {
                             Icon(Icons.Default.LocationOn, contentDescription = null,
                               )
                         },
                         trailingIcon = {
                             IconButton(onClick = {
                                 viewModel.getData(city)
                                 keyboardController?.hide()
                             }) {
                                 Icon(Icons.Default.Search, contentDescription = "Search",
                                    )
                             }
                         },
                     keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                         imeAction = ImeAction.Search
                     ),
                     keyboardActions = KeyboardActions(onSearch = {
                         viewModel.getData(city)
                         keyboardController?.hide()
                     })

                         )
             }

             // 📦 Weather Data Section
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .padding(top = 8.dp),
                 contentAlignment = Alignment.Center
             ) {
                 when (val result = weatherResult.value) {
                     is NetworkResponse.Loading -> {
                         CircularProgressIndicator(
                             color = Color.Cyan,
                             strokeWidth = 4.dp
                         )
                     }
                     is NetworkResponse.Error -> {
                         Column(modifier = Modifier.fillMaxWidth(),
                             verticalArrangement = Arrangement.Center,
                             horizontalAlignment = Alignment.CenterHorizontally){
                             Icon(
                                 painter = painterResource(R.drawable.baseline_cloud_off_24),
                                 contentDescription = "fail to load",
                                 modifier = Modifier.size(100.dp),
                                 tint = Color.Gray

                             )
                             Text(
                                 text ="Failed to load weather",
                                 fontSize = 18.sp,
                                 textAlign = TextAlign.Center,
                                 color = Color.Gray
                             )
                         }
                     }
                     is NetworkResponse.Success -> {
                         WeatherDetails(result.data)

                     }
                     null -> {
                         Column(
                             horizontalAlignment = Alignment.CenterHorizontally,
                             verticalArrangement = Arrangement.Center
                         ) {
                             Icon(painter = painterResource(R.drawable.weather),
                                 contentDescription = "weather logo",
                                 modifier = Modifier.size(120.dp))
                             Spacer(modifier = Modifier.height(12.dp))
                             Text(
                                 text = "Search a city to view weather updates!",
                                 fontSize = 18.sp,
                                 color = Color.Gray,
                                 fontFamily = FontFamily.Monospace,
                                 textAlign = TextAlign.Center
                             )
                         }
                     }


                 }
             }
         }

    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 📍 Location Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color.Blue)
            Text(
                text = "${data.location.name}, ${data.location.country}",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        // 🌡️ Temperature & Icon
        Text(
            text = "${data.current.temp_c}°C",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold
        )
        val iconUrl = data.current.condition.icon.let {
            if (it.startsWith("http")) it else "https:$it"
        }.replace("64x64", "128x128")

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(iconUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        // 📊 Weather Stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherKeyVal("Humidity", "${data.current.humidity}%")
                    WeatherKeyVal("Wind", "${data.current.wind_kph} km/h")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherKeyVal("UV", data.current.uv)
                    WeatherKeyVal("Precip.", "${data.current.precip_mm} mm")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherKeyVal("Time", data.location.localtime.split(" ")[1])
                    WeatherKeyVal("Date", data.location.localtime.split(" ")[0])
                }
            }
        }
    }
}

@Composable
fun WeatherKeyVal(key: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = key, color = Color.Gray)
    }
}

