package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    private HashMap<String, String> mapDaysOfWeek = new HashMap<>();
    {
        mapDaysOfWeek.put("Воскресенье", "В воскресенье");
        mapDaysOfWeek.put("Понедельник", "В понедельник");
        mapDaysOfWeek.put("Вторник", "Во вторник");
        mapDaysOfWeek.put("Среда", "В среду");
        mapDaysOfWeek.put("Четверг", "В четверг");
        mapDaysOfWeek.put("Пятница", "В пятницу");
        mapDaysOfWeek.put("Суббота", "В субботу");
        mapDaysOfWeek.put("Сегодня", "Сегодня");
        mapDaysOfWeek.put("Завтра", "Завтра");
    }

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
        Utils.fetchSvg(getContext(), weather.icon,
                (ImageView) convertView.findViewById(R.id.adapter_item_icon));

        //строю график
        drawGraph(convertView, weather);

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

    private void drawGraph(View convertView, Weather weather)
    {
        GraphView graph = convertView.findViewById(R.id.graph);

        if(!graph.getSeries().isEmpty())
            return;

        DataPoint[] dataPoint = new DataPoint[weather.hourlyWeather.length];

        for (int i = 0; i < weather.hourlyWeather.length; i ++) {
            dataPoint[i] = new DataPoint(i, weather.hourlyWeather[i].temp);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);

        //меняю цвет линии и под линией
        series.setColor(Color.rgb(255, 195, 31));
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(50 ,255, 195, 31));

        //меняю радиус точки
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);

        //меняю название графика
        series.setTitle("Температура");

        final String finalDay = day;
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getContext(),
                        mapDaysOfWeek.get(finalDay) + " в "+ toTimeFormat(dataPoint.getX()) +
                                " температура будет равна " +
                                toDegreeFormat(dataPoint.getY()),
                        Toast.LENGTH_LONG).show();
            }
        });

        graph.addSeries(series);

        //задаю максимальное значение графика
        
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(23);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return toTimeFormat(Double.parseDouble(super.formatLabel(value, isValueX)));
                }else {
                    return toDegreeFormat(super.formatLabel(value, isValueX));
                }
            }
        });
    }

    private String toTimeFormat(Double data){
        return toTimeFormat(Integer.toString((int)Math.round(data)));
    }

    private String toTimeFormat(String string){
        if(Double.parseDouble(string) > 23)
            return "";

        return (string.length() == 1 ? "0" + string :string) + ":00";
    }

    private String toDegreeFormat(Double data){
        return toDegreeFormat(Integer.toString((int)Math.round(data)));
    }

    private String toDegreeFormat(String string){
        return string + "°C";
    }
}
