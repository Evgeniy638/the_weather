package com.example.myapplication.Data;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

public class PartDay {
    public final int pressure_mm;
    public final int humidity;
    public final String name;
    public final int temp;
    public final String icon;
    public final String condition;

    //с помощью нее я разделяю данные в массиве при конвертации массива полей класса в строку
    //так же использую ее в обратной операции
    private final String delimiter = ";";

    public PartDay(int pressure_mm, int humidity,
                   String name, int temp, String icon, String condition) {
        this.pressure_mm = pressure_mm;
        this.humidity = humidity;
        this.name = name;
        this.temp = temp;
        this.icon = icon;
        this.condition = condition;
    }

    public PartDay(String data){
        String[] s = data.split(delimiter);

        this.pressure_mm = Integer.parseInt(s[0]);
        this.humidity = Integer.parseInt(s[1]);
        this.name = s[2];
        this.temp = Integer.parseInt(s[3]);
        this.icon = s[4];
        this.condition = s[5];
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public String toString() {
        return String.join(delimiter, new String[]{
                Integer.toString(pressure_mm),
                Integer.toString(humidity),
                name,
                Integer.toString(temp),
                icon,
                condition
        });
    }
}
