package com.example.myapplication.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class Weather  implements Parcelable {
    public final int temp;
    public final int feels_like;
    public final String icon;
    public final String condition;
    public final HourlyWeather[] hourlyWeather;
    public final PartDay[] parts;
    public final long date;

    public static final int LENGTH_PART_DAY = 4;

    public Weather(int temp, int feels_like, String icon, String condition,
                   HourlyWeather[] hourlyWeather, long date, PartDay[] parts) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.icon = icon;
        this.condition = condition;
        this.hourlyWeather = hourlyWeather;
        this.date = date;
        this.parts = parts;
    }

    public Weather(Parcel in){
        int hourlyWeatherLength = in.readInt(); // считываю размер массива hourlyWeather
        int partsLength = in.readInt(); // считываю размер массива parts

        String[] data = new String[partsLength + hourlyWeatherLength + 5];
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

        this.parts = new PartDay[partsLength];
        for (int i = 0; i < partsLength; i++) {
            this.parts[i] = new PartDay(data[i + 5 + hourlyWeatherLength]);
        }
    }

    public int getMinTemperature(){
        if(hourlyWeather.length != 24){
            if (parts.length == 0) return 0;

            int min = parts[0].temp;

            for (int i = 1; i < parts.length; i++) {
                if(min > parts[i].temp)
                    min = parts[i].temp;
            }

            return min;
        }
        int min = hourlyWeather[0].temp;

        for (int i = 1; i < hourlyWeather.length; i++) {
            if(min > hourlyWeather[i].temp)
                min = hourlyWeather[i].temp;
        }

        return min;
    }

    public int getMaxTemperature(){
        if(hourlyWeather.length != 24) {
            if (parts.length == 0) return 1;

            int max = parts[0].temp;

            for (int i = 1; i < parts.length; i++) {
                if(max < parts[i].temp)
                    max = parts[i].temp;
            }

            return max;
        }

        int max = hourlyWeather[0].temp;

        for (int i = 1; i < hourlyWeather.length; i++) {
            if(max < hourlyWeather[i].temp)
                max = hourlyWeather[i].temp;
        }

        return max;
    }

    public int getMinPressure(){
        if(hourlyWeather.length != 24){
            if (parts.length == 0) return 0;

            int min = parts[0].pressure_mm;

            for (int i = 1; i < parts.length; i++) {
                if(min > parts[i].pressure_mm)
                    min = parts[i].pressure_mm;
            }

            return min;
        }
        int min = hourlyWeather[0].pressure_mm;

        for (int i = 1; i < hourlyWeather.length; i++) {
            if(min > hourlyWeather[i].pressure_mm)
                min = hourlyWeather[i].pressure_mm;
        }

        return min;
    }

    public int getMaxPressure(){
        if(hourlyWeather.length != 24) {
            if (parts.length == 0) return 1;

            int max = parts[0].pressure_mm;

            for (int i = 1; i < parts.length; i++) {
                if(max < parts[i].pressure_mm)
                    max = parts[i].pressure_mm;
            }

            return max;
        }

        int max = hourlyWeather[0].pressure_mm;

        for (int i = 1; i < hourlyWeather.length; i++) {
            if(max < hourlyWeather[i].pressure_mm)
                max = hourlyWeather[i].pressure_mm;
        }

        return max;
    }

    public int getMinHumidity(){
        if(hourlyWeather.length != 24){
            if (parts.length == 0) return 0;

            int min = parts[0].humidity;

            for (int i = 1; i < parts.length; i++) {
                if(min > parts[i].humidity)
                    min = parts[i].humidity;
            }

            return min;
        }
        int min = hourlyWeather[0].humidity;

        for (int i = 1; i < hourlyWeather.length; i++) {
            if(min > hourlyWeather[i].humidity)
                min = hourlyWeather[i].humidity;
        }

        return min;
    }

    public int getMaxHumidity(){
        if(hourlyWeather.length != 24) {
            if (parts.length == 0) return 1;

            int max = parts[0].humidity;

            for (int i = 1; i < parts.length; i++) {
                if(max < parts[i].humidity)
                    max = parts[i].humidity;
            }

            return max;
        }

        int max = hourlyWeather[0].humidity;

        for (int i = 1; i < hourlyWeather.length; i++) {
            if(max < hourlyWeather[i].humidity)
                max = hourlyWeather[i].humidity;
        }

        return max;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] data = new String[5 + hourlyWeather.length + parts.length];
        data[0] = Integer.toString(temp);
        data[1] = Integer.toString(feels_like);
        data[2] = icon;
        data[3] = condition;
        data[4] = Long.toString(date);

        for (int i = 0; i < hourlyWeather.length; i++) {
            data[i + 5] = hourlyWeather[i].toString();
        }

        for (int i = 0; i < parts.length; i++) {
            data[i + 5 + hourlyWeather.length] = parts[i].toString();
        }

        dest.writeInt(hourlyWeather.length);
        dest.writeInt(parts.length);
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
