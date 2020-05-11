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

import java.util.ArrayList;
import java.util.Date;

public class MyWeatherAdapter extends ArrayAdapter<Weather> {
    private ArrayList<Weather> weathers;
    private static ArrayList<String> modes = new ArrayList<>();
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

        for (int i = 0; i < weathers.size(); i++) {
            modes.add(UtilsView.MODE_TEMPERATURE);
        }
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Weather weather = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.adapter_item, null);
        }

        final View finalConvertView = convertView;

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
        UtilsView.drawGraph(modes.get(position),
                (CustomGraphView) convertView.findViewById(R.id.graph),
                weather, getContext(), day);

        //////кнопки смены режима данных графика//////

        //меняем на температуру
        convertView.findViewById(R.id.adapter_button_temp)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modes.set(position, UtilsView.MODE_TEMPERATURE);
                UtilsView.drawGraph(modes.get(position),
                        (CustomGraphView) finalConvertView.findViewById(R.id.graph),
                        weather, getContext(), day);
            }
        });

        //меняем на влажность
        convertView.findViewById(R.id.adapter_button_humidity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modes.set(position, UtilsView.MODE_HUMIDITY);
                        UtilsView.drawGraph(modes.get(position),
                                (CustomGraphView) finalConvertView.findViewById(R.id.graph),
                                weather, getContext(), day);
                    }
                });

        //меняем на давление
        convertView.findViewById(R.id.adapter_button_pressure_mm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modes.set(position, UtilsView.MODE_PRESSURE);
                        UtilsView.drawGraph(modes.get(position),
                                (CustomGraphView) finalConvertView.findViewById(R.id.graph),
                                weather, getContext(), day);
                    }
                });

        //////показ или сокрытие дополнительных данных//////
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
                intent.putExtra("position", position);
                getContext().startActivity(intent);

                return false;
            }
        });

        return convertView;
    }



    public static void setModes(int position, String mode){
        if(position < 0 || position >= modes.size())
            return;

        modes.set(position, mode);
    }

    public static String getMode(int position){
        if(position < 0 || position >= modes.size())
            return "";

        return modes.get(position);
    }
}
