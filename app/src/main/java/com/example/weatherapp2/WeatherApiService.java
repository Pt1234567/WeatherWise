package com.example.weatherapp2;

import com.example.weatherapp2.Model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {


    //this func will get weather forecast for particular city
    @GET("weather")
    Call<WeatherResponse> getWeatherData(
            @Query("q") String city,
            @Query("appid") String apiKey
    );

}
