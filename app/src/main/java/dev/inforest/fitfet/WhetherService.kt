package dev.inforest.fitfet

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun current(@Query("lat") lat: Double, @Query("lon") lng: Double, @Query("appid") appId: String): Weathers

    @GET("data/2.5/forecast")
    suspend fun forecast(@Query("lat") lat: Double, @Query("lon") lng: Double, @Query("appid") appId: String): WeatherApi
}

data class WeatherApi(
    val list: List<Weathers>,
    val city: City,
)

data class Weathers(
    val dt: Long,
    val weather: List<Weather>,
    val main: Main,
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp_min: Double,
    val temp_max: Double
)

data class City(
    val name: String
)