package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyWeatherAdapter extends ArrayAdapter<Weather> {
    private ArrayList<Weather> weathers;
    private String[] daysOfWeek = {
            "Понедельник",
            "Вторник",
            "Среда",
            "Четверг",
            "Пятница",
            "Суббота",
            "Воскресенье"
    };

    public MyWeatherAdapter(@NonNull Context context, ArrayList<Weather> weathers) {
        super(context, R.layout.adapter_item);
        this.weathers = weathers;
    }

    @Override
    public int getCount() {
        return weathers.size();
    }

    @Nullable
    @Override
    public Weather getItem(int position) {
        return weathers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Weather weather = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.adapter_item, null);
        }

        ((TextView) convertView.findViewById(R.id.adapter_item_day_of_week))
                .setText(daysOfWeek[position] + ":");
        ((TextView) convertView.findViewById(R.id.adapter_item_temperature))
                .setText(weather.temp + "°C");
        Utils.fetchSvg(getContext(), weather.icon,
                (ImageView) convertView.findViewById(R.id.adapter_item_icon));

        return convertView;
    }
}
