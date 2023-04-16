package dev.inforest.fitfet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import dev.inforest.fitfet.ui.theme.WhetherTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ComponentActivity() {
    
    suspend fun loadApis(): List<WeatherApi> {
        val apis = ArrayList<WeatherApi>()
        for (city in ECity.values()) {
            apis.add(MainApplication.weatherService.forecast(city.lat, city.lng, "e81e90f3288d5b37a069ef574a4fa5c1"))
        }
        return apis
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhetherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ListWhether(Modifier.fillMaxSize())
                }
            }
        }
    }

    @Composable
    fun ListWhether(modifier: Modifier) {
        val coroutineScope = rememberCoroutineScope()
        val listWeather = remember { mutableStateOf<List<WeatherApi>>(arrayListOf()) }
        LaunchedEffect("weather") {
            coroutineScope.launch {
                listWeather.value = loadApis()
            }
        }
        LazyColumn(modifier) {
            items(listWeather.value) { api ->
                val dates = api.list.distinctBy { dtToDate(it.dt) }
                LazyColumn(Modifier.height((dates.count() * 70 + 60).dp), userScrollEnabled = false) {
                    item {
                        Row(Modifier.height(60.dp)) {
                            Text(api.city.name, Modifier.padding(start = 20.dp, top = 20.dp), style = MaterialTheme.typography.body1)
                        }
                    }
                    items(dates) { weather ->
                        Box(Modifier.height(70.dp)) {
                            Row(Modifier.align(Center)) {
                                Image(
                                    painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/${weather.weather.firstOrNull()?.icon?.replace("n", "d") ?: ""}@2x.png"),
                                    contentDescription = null,
                                    modifier = Modifier.padding(start = 16.dp).size(50.dp).align(CenterVertically)
                                )
                                Column(Modifier.padding(start = 10.dp).align(CenterVertically)) {
                                    Text(dtToDate(weather.dt), style = MaterialTheme.typography.subtitle1)
                                    Text(weather.weather.firstOrNull()?.main ?: "", style = MaterialTheme.typography.subtitle2)
                                }
                                Spacer(Modifier.weight(1f))
                                Row(Modifier.align(Bottom).padding(end = 20.dp)) {
                                    Text("Max: ${kelvinToCelsiusString(weather.main.temp_max)}", style = MaterialTheme.typography.subtitle2)
                                    Text("Min: ${kelvinToCelsiusString(weather.main.temp_min)}", Modifier.padding(start = 10.dp), style = MaterialTheme.typography.subtitle2)
                                }
                            }
                            Box(Modifier.padding(horizontal = 20.dp).fillMaxWidth().height(1.dp).align(BottomCenter).background(MaterialTheme.colors.secondary))
                        }
                    }
                }
            }
        }
    }

    fun dtToDate(dt: Long): String {
        val sdf = SimpleDateFormat("EEE dd MMM", Locale.US)
        val date = Date(dt * 1000)
        return sdf.format(date)
    }

    fun kelvinToCelsiusString(kelvin: Double): String = "${(kelvin - 273.15).toInt()}°C"
}