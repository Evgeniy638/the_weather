package com.example.myapplication.Utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.example.myapplication.CustomGraphView;
import com.example.myapplication.Data.Weather;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.HashMap;

public class UtilsView {
    private static final int MAX_Y_DIFFERENCE = 8;

    public static final String MODE_TEMPERATURE = "MODE_TEMPERATURE";
    public static final String MODE_HUMIDITY = "MODE_HUMIDITY";
    public static final String MODE_PRESSURE = "MODE_PRESSURE";

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

    private static String[] partsDayName = new String[Weather.LENGTH_PART_DAY];
    private static HashMap<String, String[]> mapPartDay = new HashMap<>();
    static {
        mapPartDay.put("night", new String[]{"ночь", "ночью"});
        mapPartDay.put("morning", new String[]{"утро", "утром"});
        mapPartDay.put("day", new String[]{"день", "днем"});
        mapPartDay.put("evening", new String[]{"вечер", "вечером"});
    }

    public static void drawGraph(final String mode, CustomGraphView graph, Weather weather,
                                 final Context context, String day)
    {
        if (!checkMode(mode))
            return;

        graph.init();

        final boolean isHour = weather.hourlyWeather.length == 24;

        DataPoint[] dataPoint = new DataPoint[isHour
                ?weather.hourlyWeather.length
                :weather.parts.length];

        for (int i = 0; i < dataPoint.length; i ++) {
            dataPoint[i] = new DataPoint(i,
                    isHour
                    ?getHourlyWeatherByMode(mode, weather, i)
                    :getPartsByMode(mode, weather, i));

            if(!isHour){
                partsDayName[i] = weather.parts[i].name;
            }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);

        //меняю цвет линии и под линией
        series.setColor(Color.rgb(255, 195, 31));
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(50 ,255, 195, 31));

        //меняю радиус точки
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);

        //показ сообения при нажатии на точку
        final String finalDay = day;
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                String typeInf = "";

                if(mode.equals(UtilsView.MODE_PRESSURE)){
                    typeInf = "давление будет равно";
                } else if (mode.equals(UtilsView.MODE_HUMIDITY)){
                    typeInf = "влажность будет равна";
                } else if (mode.equals(UtilsView.MODE_TEMPERATURE)){
                    typeInf = "температура будет равна";
                }

                String str = isHour
                        ?" в "+ toTimeFormat(dataPoint.getX())
                        :" " + mapPartDay.get(partsDayName[(int)dataPoint.getX()])[1];

                Toast.makeText(context,
                        mapDaysOfWeek.get(finalDay) + str +
                                " " + typeInf + " " +
                                toFormatByMode(dataPoint.getY(), mode),
                        Toast.LENGTH_LONG).show();
            }
        });

        graph.addSeries(series);

        //////задаю границы и шаг значений осей//////
        graph.getGridLabelRenderer().setHumanRounding(false);

        //для оси OY
        int max = getMaxData(mode, weather);
        int min = getMinData(mode, weather);
        graph.getGridLabelRenderer().setNumVerticalLabels(MAX_Y_DIFFERENCE + 1);
        graph.getViewport().setMaxY(Math.max(max, min + MAX_Y_DIFFERENCE));
        graph.getViewport().setMinY(min);
        graph.getViewport().setYAxisBoundsManual(true);

        //для оси OX
        graph.getGridLabelRenderer().setNumHorizontalLabels(isHour ?5 :Weather.LENGTH_PART_DAY);
        graph.getViewport().setMaxX(dataPoint.length - 1);
        graph.getViewport().setXAxisBoundsManual(true);

        //лучше удалить
        //разрешаю прокрутку и увелечение
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        //////изменяю формат значений осей//////
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return isHour
                            ?toTimeFormat(Double.parseDouble(super.formatLabel(value, isValueX)))
                            :toPartDayFormat(super.formatLabel(value, isValueX));
                }else {
                    return toFormatByMode(super.formatLabel(value, isValueX), mode);
                }
            }
        });
    }

    private static int getMinData(String mode, Weather weather) {
        if (mode.equals(MODE_TEMPERATURE)){
            return weather.getMinTemperature();
        }else if (mode.equals(MODE_HUMIDITY)){
            return weather.getMinHumidity();
        }else {
            return weather.getMinPressure();
        }
    }

    private static int getMaxData(String mode, Weather weather) {
        if (mode.equals(MODE_TEMPERATURE)){
            return weather.getMaxTemperature();
        }else if (mode.equals(MODE_HUMIDITY)){
            return weather.getMaxHumidity();
        }else {
            return weather.getMaxPressure();
        }
    }

    private static boolean checkMode(String mode){
        return mode.equals(MODE_TEMPERATURE) || mode.equals(MODE_HUMIDITY)
                || mode.equals(MODE_PRESSURE);
    }

    private static int getHourlyWeatherByMode(String mode, Weather weather, int index){
        if (mode.equals(MODE_TEMPERATURE)){
            return weather.hourlyWeather[index].temp;
        }else if (mode.equals(MODE_HUMIDITY)){
            return weather.hourlyWeather[index].humidity;
        }else {
            return weather.hourlyWeather[index].pressure_mm;
        }
    }

    private static int getPartsByMode(String mode, Weather weather, int index){
        if (mode.equals(MODE_TEMPERATURE)){
            return weather.parts[index].temp;
        }else if (mode.equals(MODE_HUMIDITY)){
            return weather.parts[index].humidity;
        }else {
            return weather.parts[index].pressure_mm;
        }
    }

    private static String toPartDayFormat(String string){
        int data = (int)Math.round(Double.parseDouble(string));

        String result = mapPartDay.get(partsDayName[data])[0];

        return result;
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

    private static String toFormatByMode(Double data, String mode){
        return toFormatByMode(Double.toString(data), mode);
    }

    private static String toFormatByMode(String data, String mode){
        data = Integer.toString((int)Math.round(Double.parseDouble(data)));

        if (mode.equals(UtilsView.MODE_TEMPERATURE)){
            return toDegreeFormat(data);
        }else if(mode.equals(UtilsView.MODE_HUMIDITY)){
            return toHumadityFormat(data);
        }else if(mode.equals(UtilsView.MODE_PRESSURE)){
            return toPressureFormat(data);
        }else {
            return data;
        }
    }

    private static String toDegreeFormat(String data){
        return data + "°C";
    }

    private static String toHumadityFormat(String data){
        return data + "%";
    }

    private static String toPressureFormat(String data){
        return data + " мм рт.ст.";
    }
}
