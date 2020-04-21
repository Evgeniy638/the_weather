package com.example.myapplication;

public class Weather {
    public final int temp;
    public final int feels_like;
    public final String icon;
    public final String condition;
    public final HourlyWeather[] hourlyWeather;

    public Weather(int temp, int feels_like, String icon, String condition, HourlyWeather[] hourlyWeather) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.icon = icon;
        this.condition = condition;
        this.hourlyWeather = hourlyWeather;
    }
}
