package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Data.Weather;
import com.example.myapplication.Utils.UtilsHTTP;
import com.example.myapplication.Utils.UtilsView;
import com.jjoe64.graphview.GraphView;

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
    private String day;

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
        return weathers.get(position).date;
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

        //заполняю данные
        assert weather != null;

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
        UtilsHTTP.fetchSvg(getContext(), weather.icon,
                (ImageView) convertView.findViewById(R.id.adapter_item_icon));

        //строю график
        UtilsView.drawGraph((GraphView) convertView.findViewById(R.id.graph), weather, getContext(), day);

        //////показ или сокрытие дополнительных данных//////
        final View finalConvertView = convertView;
        LinearLayout toggle = convertView.findViewById(R.id.adapter_toggle_additional_information);

        //открытие скрытого окна
        toggle.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        LinearLayout toggleAdditionalInformation = finalConvertView
                                .findViewById(R.id.adapter_toggle_additional_information);

                        LinearLayout additionalInformation = finalConvertView
                                .findViewById(R.id.adapter_additional_information);

                        ViewGroup.LayoutParams informationParams = additionalInformation
                                .getLayoutParams();

                        if (additionalInformation.getHeight() == 0){
                            toggleAdditionalInformation.setBackground(getContext().getResources()
                                            .getDrawable(R.drawable.riple_with_top_radius));

                            informationParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        } else {
                            toggleAdditionalInformation.setBackground(getContext().getResources()
                                    .getDrawable(R.drawable.ripple));
                            informationParams.height = 0;
                        }

                        additionalInformation.setLayoutParams(informationParams);
                    }
                });

        //открыть на весь экран
        toggle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(), WeatherActivity.class);
                intent.putExtra("weather", weather);
                intent.putExtra("day", day);
                getContext().startActivity(intent);

                return false;
            }
        });

        return convertView;
    }
}
