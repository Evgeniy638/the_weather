package com.example.myapplication;

public class HourlyWeather {
    public final int temp;
    public final String icon;
    public final String condition;

    public HourlyWeather(int temp, String icon, String condition) {
        this.temp = temp;
        this.icon = icon;
        this.condition = condition;
    }
}
