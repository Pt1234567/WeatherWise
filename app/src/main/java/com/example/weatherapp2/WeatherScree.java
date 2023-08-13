package com.example.weatherapp2;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.weatherapp2.Model.Weather;
import com.example.weatherapp2.Model.WeatherResponse;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherScree extends AppCompatActivity {
    TextView temp, maxTemp, minTemp, condition, day, date, wind, rain, humidity, sunrise, sunset, sea, location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_scree);
        getSupportActionBar().hide();

        temp = findViewById(R.id.temp);
        maxTemp = findViewById(R.id.maxTemp);
        minTemp = findViewById(R.id.minTemp);
        condition = findViewById(R.id.condition);
        day = findViewById(R.id.Day);
        date = findViewById(R.id.date);
        wind = findViewById(R.id.wind);
        rain = findViewById(R.id.rain);
        humidity = findViewById(R.id.humidity);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        sea = findViewById(R.id.sea);
        location = findViewById(R.id.location);
        getWeatherForecast("Hamirpur");
        SearchCity();

    }

    private void SearchCity() {
        SearchView searchView=findViewById(R.id.search);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
              if(query!=null){
                  getWeatherForecast(query);
              }
              return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getWeatherForecast(String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService weatherApiService = retrofit.create(WeatherApiService.class);

        Call<WeatherResponse> call = weatherApiService.getWeatherData(query, "d0833b7ed3b9afd62069624d21f3d569");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                try {

                    if (response.isSuccessful()) {

                        WeatherResponse weatherResponse = response.body();



                        temp.setText(String.valueOf(weatherResponse.getMain().getTemp()) + "K");
                        maxTemp.setText("Max Temp: " + String.valueOf(weatherResponse.getMain().getTemp_max()) + "K");
                        minTemp.setText("Min Temp: " + String.valueOf(weatherResponse.getMain().getTemp_min()) + "K");
                        humidity.setText(String.valueOf(weatherResponse.getMain().getHumidity() + "%"));
                        wind.setText(String.valueOf(weatherResponse.getWind().getSpeed()) + "m/s");
                        sea.setText(String.valueOf(weatherResponse.getMain().getPressure()) + "hpa");
                        condition.setText(String.valueOf(weatherResponse.getWeathers()));
                        sunset.setText(String.valueOf(SunRiseAndSet(weatherResponse.getSys().getSunset())));
                        sunrise.setText(String.valueOf(SunRiseAndSet(weatherResponse.getSys().getSunrise())));
                          day.setText(String.valueOf(Day(System.currentTimeMillis())));
                          date.setText(String.valueOf(datee(System.currentTimeMillis())));
                         location.setText(String.valueOf(weatherResponse.getName()));
                        if (weatherResponse != null && weatherResponse.getWeathers() != null && !weatherResponse.getWeathers().isEmpty()) {

                            condition.setText(String.valueOf(weatherResponse.getWeathers().get(0).getMain()));
                            rain.setText(String.valueOf(weatherResponse.getWeathers().get(0).getMain()));
                            // ...
                            chaneBackGroundAccordingToCondition(weatherResponse.getWeathers().get(0).getMain());
                        }else{
                            Log.e("Weather Data", "Weather data is null or empty");
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void chaneBackGroundAccordingToCondition(String condition) {
                ConstraintLayout constraintLayout=findViewById(R.id.parentBackground);
                LottieAnimationView lottieAnimationView=findViewById(R.id.lottie);


                if(condition.equals("Clear Sky")||condition.equals("Sunny")||condition.equals("Clear")){
                    Log.e("Weather app","Setting background");
                    constraintLayout.setBackgroundResource(R.drawable.s);
                    lottieAnimationView.setAnimation(R.raw.sun);
                }
                else if(condition.equals("Haze")||condition.equals("Partly Cloudy")||condition.equals("Clouds")||condition.equals("Overcast")||condition.equals("Mist")||condition.equals("Foggy")){
                    constraintLayout.setBackgroundResource(R.drawable.cloudy);
                    lottieAnimationView.setAnimation(R.raw.cloudy);
                }
                else if(condition.equals("Rain")||condition.equals("Drizzle")||condition.equals("Moderate Rain")||condition.equals("Showers")||condition.equals("Heavy Rain")){
                    constraintLayout.setBackgroundResource(R.drawable.rainroad);
                    lottieAnimationView.setAnimation(R.raw.rainy);
                }
                else if(condition.equals("Light Snow")||condition.equals("Moderate Snow")||condition.equals("Heavy Snow")||condition.equals("Blizzard")) {
                    constraintLayout.setBackgroundResource(R.drawable.snow);
                    lottieAnimationView.setAnimation(R.raw.snow);
                }
            lottieAnimationView.playAnimation();

            }


            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
    }

    private String SunRiseAndSet(long timestamp) {
        try {
            // Convert the Unix timestamp to milliseconds
            Date date = new Date(timestamp * 1000);

            // Define the desired date and time format
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

            // Format the date and time as a string
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Timestamp";
        }
    }

    private String datee(long timestamp) {
        try {
            // Convert the Unix timestamp to milliseconds
        Date date1=new Date();

            // Define the desired date and time format
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

            // Format the date and time as a string
            return sdf.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Timestamp";
        }
    }

    private String Day(long timestamp) {
        try {
            // Define the desired date and time format
            Date date1=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());

            // Format the date and time as a string
            return sdf.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Timestamp";
        }
    }


}
