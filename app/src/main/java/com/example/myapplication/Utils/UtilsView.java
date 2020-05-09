package com.example.myapplication.Utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.example.myapplication.Data.Weather;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.HashMap;

public class UtilsView {
    private static HashMap<String, String> mapDaysOfWeek = new HashMap<>();
    static {
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

    public static void drawGraph(GraphView graph, Weather weather, final Context context, String day)
    {
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
                Toast.makeText(context,
                        mapDaysOfWeek.get(finalDay) + " в "+ toTimeFormat(dataPoint.getX()) +
                                " температура будет равна " +
                                toDegreeFormat(dataPoint.getY()),
                        Toast.LENGTH_LONG).show();
            }
        });

        graph.addSeries(series);

        //////задаю границы и шаг значений осей//////
        graph.getGridLabelRenderer().setHumanRounding(false);

        //для оси OY
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(weather.getMaxTemperature());
        graph.getViewport().setMinY(weather.getMinTemperature());
        graph.getGridLabelRenderer().setNumVerticalLabels(Math.min(weather.getMaxTemperature() -
                weather.getMinTemperature() + 1, 8));

        //для оси OY
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(23);
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);



        //////изменяю формат значений осей//////
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return toTimeFormat(Double.parseDouble(super.formatLabel(value, isValueX)));
                }else {
                    return toDegreeFormat(Double.parseDouble(super.formatLabel(value, isValueX)));
                }
            }
        });
    }

    private static String toTimeFormat(Double data){
        return toTimeFormat(Integer.toString((int)Math.round(data)));
    }

    private static String toTimeFormat(String string){
        if(Double.parseDouble(string) > 23)
            return "";

        return (string.length() == 1 ? "0" + string :string) + ":00";
    }

    private static String toDegreeFormat(Double data){
        return toDegreeFormat(Integer.toString((int)Math.round(data)));
    }

    private static String toDegreeFormat(String string){
        return string + "°C";
    }
}
