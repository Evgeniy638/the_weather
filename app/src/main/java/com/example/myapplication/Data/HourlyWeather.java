package com.example.myapplication.Data;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

public class HourlyWeather {
    public final int temp;
    public final String icon;
    public final String condition;
    public final long hour_ts;

    //с помощью нее я разделяю данные в массиве при конвертации массива полей класса в строку
    //так же использую ее в обратной операции
    private final String delimiter = ";";

    public HourlyWeather(int temp, String icon, String condition, long hour_ts) {
        this.temp = temp;
        this.icon = icon;
        this.condition = condition;
        this.hour_ts = hour_ts;
    }

    public HourlyWeather(String data){
        String[] s = data.split(delimiter);

        this.temp = Integer.parseInt(s[0]);
        this.icon = s[1];
        this.condition = s[2];
        this.hour_ts = Long.parseLong(s[3]);
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public String toString() {
        return String.join(delimiter, new String[]{
                Integer.toString(temp),
                icon,
                condition,
                Long.toString(hour_ts)
        });
    }
}
