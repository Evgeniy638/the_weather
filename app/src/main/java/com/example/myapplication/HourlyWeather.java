package com.example.myapplication;

public class HourlyWeather {
    public final int temp;
    public final String icon;
    public final String condition;
    public final long hour_ts;

    public HourlyWeather(int temp, String icon, String condition, long hour_ts) {
        this.temp = temp;
        this.icon = icon;
        this.condition = condition;
        this.hour_ts = hour_ts;
    }
}
