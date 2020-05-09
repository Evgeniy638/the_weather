package com.example.myapplication.Data;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

public class PartDay {
    public final String name;
    public final int temp;
    public final String icon;
    public final String condition;

    //с помощью нее я разделяю данные в массиве при конвертации массива полей класса в строку
    //так же использую ее в обратной операции
    private final String delimiter = ";";

    public PartDay(String name, int temp, String icon, String condition) {
        this.name = name;
        this.temp = temp;
        this.icon = icon;
        this.condition = condition;
    }

    public PartDay(String data){
        String[] s = data.split(delimiter);

        this.name = s[0];
        this.temp = Integer.parseInt(s[1]);
        this.icon = s[2];
        this.condition = s[3];
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public String toString() {
        return String.join(delimiter, new String[]{
                name,
                Integer.toString(temp),
                icon,
                condition
        });
    }
}
