package com.example.myapplication.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class Weather  implements Parcelable {
    public final int temp;
    public final int feels_like;
    public final String icon;
    public final String condition;
    public final HourlyWeather[] hourlyWeather;
    public final long date;

    public Weather(int temp, int feels_like, String icon, String condition,
                   HourlyWeather[] hourlyWeather, long date) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.icon = icon;
        this.condition = condition;
        this.hourlyWeather = hourlyWeather;
        this.date = date;
    }

    public Weather(Parcel in){
        int hourlyWeatherLength = in.readInt();

        String[] data = new String[hourlyWeatherLength + 5];
        in.readStringArray(data);

        this.temp = Integer.parseInt(data[0]);
        this.feels_like = Integer.parseInt(data[1]);
        this.icon = data[2];
        this.condition = data[3];
        this.date = Long.parseLong(data[4]);

        this.hourlyWeather = new HourlyWeather[hourlyWeatherLength];
        for (int i = 0; i < hourlyWeatherLength; i++) {
            this.hourlyWeather[i] = new HourlyWeather(data[i + 5]);
        }
    }

    public int getMinTemperature(){
        if(hourlyWeather.length == 0) return 0;
        int min = hourlyWeather[0].temp;

        for (int i = 1; i < hourlyWeather.length; i++) {
            if(min > hourlyWeather[i].temp)
                min = hourlyWeather[i].temp;
        }

        return min;
    }

    public int getMaxTemperature(){
        if(hourlyWeather.length == 0) return 1;

        int max = hourlyWeather[0].temp;

        for (int i = 1; i < hourlyWeather.length; i++) {
            if(max < hourlyWeather[i].temp)
                max = hourlyWeather[i].temp;
        }

        return max;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] data = new String[5 + hourlyWeather.length];
        data[0] = Integer.toString(temp);
        data[1] = Integer.toString(feels_like);
        data[2] = icon;
        data[3] = condition;
        data[4] = Long.toString(date);

        for (int i = 0; i < hourlyWeather.length; i++) {
            data[i + 5] = hourlyWeather[i].toString();
        }

        dest.writeInt(hourlyWeather.length);
        dest.writeStringArray(data);
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
