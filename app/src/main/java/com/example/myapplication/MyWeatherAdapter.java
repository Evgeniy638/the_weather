package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

    private String[] hoursString = {"00:00", "03:00", "06:00", "09:00",
            "12:00", "15:00", "18:00", "21:00"};

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

        //заполняю данные
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

        //строю график
        DataPoint[] dataPoint = new DataPoint[weather.hourlyWeather.length % 3 == 0
                ?weather.hourlyWeather.length / 3
                :weather.hourlyWeather.length / 3 + 1];

        for (int i = 0; i < weather.hourlyWeather.length; i += 3){
            dataPoint[i / 3] = new DataPoint(i / 3, weather.hourlyWeather[i].temp);
        }

        GraphView graph = convertView.findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);

        series.setDrawBackground(true);
        series.setBackgroundColor(Color.rgb(255, 195, 31));

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return super.formatLabel(value, isValueX);
                }else {
                    return super.formatLabel(value, isValueX) + "°C";
                }
            }
        });
        //закончил строить график

        //показ или сокрытие дополнительных данных
        final View finalConvertView = convertView;
        convertView.findViewById(R.id.adapter_toggle_additional_information)
                .setOnClickListener(new View.OnClickListener() {
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

        return convertView;
    }
}
