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

import com.example.myapplication.Data.AuxiliaryData;
import com.example.myapplication.Data.Weather;
import com.example.myapplication.Utils.UtilsHTTP;
import com.example.myapplication.Utils.UtilsView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyWeatherAdapter extends ArrayAdapter<Weather> {
    private ArrayList<Weather> weathers;
    private static ArrayList<String> modes = new ArrayList<>();
    private static ArrayList<Boolean> openWindows = new ArrayList<>();

    private static boolean isInit = false;

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

        if(!isInit) {
            isInit = true;

            for (int i = 0; i < weathers.size(); i++) {
                modes.add(UtilsView.MODE_TEMPERATURE);
            }

            for (int i = 0; i < weathers.size(); i++) {
                openWindows.add(false);
            }
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

        /////заполняю данные/////
        assert weather != null;

        Date date = new Date(weather.date);

        //выбераю день
        if(position == 0){
            day = "Сегодня";
        }else if (position == 1){
            day = "Завтра";
        }else {
            int indexDay = date.getDay();
            day = daysOfWeek[indexDay];
        }

        //настраиваю вывод даты
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM",
                AuxiliaryData.getDateFormatSymbols());

        //заполняем данные текстовых элементов и картинок
        ((TextView) convertView.findViewById(R.id.adapter_item_day_of_week))
                .setText(day);
        ((TextView) convertView.findViewById(R.id.adapter_item_date))
                .setText(simpleDateFormat.format(date));
        ((TextView) convertView.findViewById(R.id.adapter_item_day_temperature))
                .setText((weather.parts[2].temp > 0 ?"+" :"") + weather.parts[2].temp + "°C");
        ((TextView) convertView.findViewById(R.id.adapter_item_night_temperature))
                .setText((weather.parts[0].temp > 0 ?"+" :"") + weather.parts[0].temp + "°C");
        UtilsHTTP.fetchSvg(getContext(), weather.icon,
                (ImageView) convertView.findViewById(R.id.adapter_item_icon));
        ((TextView) convertView.findViewById(R.id.adapter_item_condition))
                .setText(AuxiliaryData.getMapConditions(weather.condition));

        //строю график
        UtilsView.drawGraph(modes.get(position),
                (CustomGraphView) convertView.findViewById(R.id.graph),
                weather, getContext(), day);

        //открываю или закрываю окно в зависимости от openWindows
        toggleHiddenWindow(finalConvertView, position);

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
                        openWindows.set(position, !openWindows.get(position));
                        toggleHiddenWindow(finalConvertView, position);
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

    @SuppressLint("NewApi")
    private void toggleHiddenWindow(@Nullable final View finalConvertView, int position){
        LinearLayout toggleAdditionalInformation = finalConvertView
                .findViewById(R.id.adapter_toggle_additional_information);

        LinearLayout additionalInformation = finalConvertView
                .findViewById(R.id.adapter_additional_information);

        ViewGroup.LayoutParams informationParams = additionalInformation
                .getLayoutParams();

        if (openWindows.get(position)){
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
