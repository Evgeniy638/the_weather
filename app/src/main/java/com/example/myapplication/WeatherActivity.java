package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Data.AuxiliaryData;
import com.example.myapplication.Data.Weather;
import com.example.myapplication.Utils.UtilsHTTP;
import com.example.myapplication.Utils.UtilsView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {
    private Weather weather;
    private String day;
    private int position;
    private Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        context = this;

        //показываем стрелочку
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //считываем данные переданные через Intent
        weather = getIntent().getParcelableExtra("weather");
        day = getIntent().getStringExtra("day");
        position = getIntent().getIntExtra("position", 0);

        //настраиваю вывод даты
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM",
                AuxiliaryData.getDateFormatSymbols());

        //записывам значение в верхний блок над графиком
        ((TextView) findViewById(R.id.activity_weather_day_of_week))
                .setText(day);
        ((TextView) findViewById(R.id.activity_weather_date))
                .setText(simpleDateFormat.format(new Date(weather.date)));
        ((TextView) findViewById(R.id.activity_weather_day_temperature))
                .setText((weather.parts[2].temp > 0 ?"+" :"") + weather.parts[2].temp + "°C");
        ((TextView) findViewById(R.id.activity_weather_night_temperature))
                .setText((weather.parts[0].temp > 0 ?"+" :"") + weather.parts[0].temp + "°C");
        UtilsHTTP.fetchSvg(getBaseContext(), weather.icon,
                (ImageView) findViewById(R.id.activity_weather_icon));
        ((TextView) findViewById(R.id.activity_weather_condition))
                .setText(AuxiliaryData.getMapConditions(weather.condition));

        //рисуем график
        UtilsView.drawGraph(MyWeatherAdapter.getMode(position),
                (CustomGraphView) findViewById(R.id.activity_weather_graph),
                weather, this, day);

        ////назначаем обработчики на кнопки////
        //переключить график в режим показа влажности
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

        //переключить график в режим показа давления
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

        //переключить график в режим показа температуры
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
