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
import java.util.Date;

public class MyWeatherAdapter extends ArrayAdapter<Weather> {
    private ArrayList<Weather> weathers;
    private String[] daysOfWeek = {
            "Воскресенье",
            "Понедельник",
            "Вторник",
            "Среда",
            "Четверг",
            "Пятница",
            "Суббота"
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

        assert weather != null;

        String day;

        if(position == 0){
            day = "Сегодня";
        }else if (position == 1){
            day = "Завтра";
        }else {
            int indexDay = (new Date(weather.date)).getDay();
            day = daysOfWeek[indexDay];
        }

        ((TextView) convertView.findViewById(R.id.adapter_item_day_of_week))
                .setText(day + ":");
        ((TextView) convertView.findViewById(R.id.adapter_item_temperature))
                .setText(weather.temp + "°C");
        Utils.fetchSvg(getContext(), weather.icon,
                (ImageView) convertView.findViewById(R.id.adapter_item_icon));

        return convertView;
    }
}
