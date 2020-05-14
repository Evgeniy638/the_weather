package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Data.Weather;
import com.example.myapplication.Utils.UtilsView;

import java.util.Objects;

public class WeatherActivity extends AppCompatActivity {
    private Weather weather;
    private String day;
    private int position;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        context = this;

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        if(toolbar != null){
//            toolbar.setTitleTextColor(getResources().getColor(R.color.textColor2));
//            setSupportActionBar(toolbar);
//        }

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        weather = getIntent().getParcelableExtra("weather");
        day = getIntent().getStringExtra("day");
        position = getIntent().getIntExtra("position", 0);

        UtilsView.drawGraph(MyWeatherAdapter.getMode(position),
                (CustomGraphView) findViewById(R.id.activity_weather_graph),
                weather, this, day);

        findViewById(R.id.activity_weather_button_humidity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyWeatherAdapter.setModes(position, UtilsView.MODE_HUMIDITY);
                        UtilsView.drawGraph(MyWeatherAdapter.getMode(position),
                                (CustomGraphView) findViewById(R.id.activity_weather_graph),
                                weather, context, day);
                    }
                });

        findViewById(R.id.activity_weather_button_pressure_mm)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWeatherAdapter.setModes(position, UtilsView.MODE_PRESSURE);
                UtilsView.drawGraph(MyWeatherAdapter.getMode(position),
                        (CustomGraphView) findViewById(R.id.activity_weather_graph),
                        weather, context, day);
            }
        });

        findViewById(R.id.activity_weather_button_temp)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWeatherAdapter.setModes(position, UtilsView.MODE_TEMPERATURE);
                UtilsView.drawGraph(MyWeatherAdapter.getMode(position),
                        (CustomGraphView) findViewById(R.id.activity_weather_graph),
                        weather, context, day);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            MainActivity.getMyWeatherAdapter().notifyDataSetChanged();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
