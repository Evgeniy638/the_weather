package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Data.Weather;
import com.example.myapplication.Utils.UtilsView;
import com.jjoe64.graphview.GraphView;

public class WeatherActivity extends AppCompatActivity {
    private Weather weather;
    private String day;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weather = getIntent().getParcelableExtra("weather");
        day= getIntent().getStringExtra("day");

        UtilsView.drawGraph((GraphView) findViewById(R.id.activity_weather_graph), weather, this, day);
    }
}
