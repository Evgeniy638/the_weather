package com.example.myapplication.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.os.Handler;
import android.os.Message;

import com.example.myapplication.Data.HourlyWeather;
import com.example.myapplication.Data.PartDay;
import com.example.myapplication.Data.Weather;

public class URLReaderWeather extends Thread{
    private final static String key = "d2e19a3a-eb4d-42a3-8e04-b76e7522c141";
    private final static String baseURL = "https://api.weather.yandex.ru/v1/forecast?limit=7&extra=true&lang=ru_RU";

    private String lat = "&lat=";
    private String lon = "&lon=";
    private Handler handler;

    private final String X_Yandex_API_Key = "X-Yandex-API-Key";

    private static HashSet<String> setPartDay = new HashSet<>();
    {
        setPartDay.add("night");
        setPartDay.add("morning");
        setPartDay.add("day");
        setPartDay.add("evening");
    }

    public URLReaderWeather(Handler handler){
        this(handler, "55.75396", "37.620393");
    }

    public URLReaderWeather(Handler handler, String lat, String lon){
        this.lat += lat;
        this.lon += lon;
        this.handler = handler;
    }

    @Override
    public void run() {
        ArrayList<Weather> weathers = getWeather();

        Message message = new Message();
        message.obj = weathers;

        handler.handleMessage(message);
    }

    private ArrayList<Weather> getWeather(){
        ArrayList<Weather> weathers = new ArrayList<>();

        try {
            URL openWeather = new URL(baseURL + lat + lon);
            URLConnection urlConnection = openWeather.openConnection();
            urlConnection.setRequestProperty(X_Yandex_API_Key, key);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            String jsonLine = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                jsonLine += inputLine;
            in.close();

            JSONObject jsonObject = new JSONObject(jsonLine);
            JSONArray forecasts = jsonObject.getJSONArray("forecasts");

            for (int day = 0; day < forecasts.length(); day++) {
                JSONObject jsonWeatherDay = forecasts.getJSONObject(day);
                JSONObject avgWeatherDay = jsonWeatherDay.getJSONObject("parts")
                        .getJSONObject("day");

                int temp = avgWeatherDay.getInt("temp_avg");
                int feels_like = avgWeatherDay.getInt("feels_like");
                String icon = avgWeatherDay.getString("icon");
                String  condition = avgWeatherDay.getString("condition");
                long date = jsonWeatherDay.getLong("date_ts") * 1000;

                //считываю данные дня по чассам
                JSONArray hours = jsonWeatherDay.getJSONArray("hours");

                HourlyWeather[] hourlyWeathers = new HourlyWeather[hours.length()];

                for (int hour = 0; hour < hours.length(); hour++) {
                    JSONObject jsonHour = hours.getJSONObject(hour);

                    hourlyWeathers[hour] = new HourlyWeather(
                            jsonHour.getInt("pressure_mm"),
                            jsonHour.getInt("humidity"),
                            jsonHour.getInt("temp"),
                            jsonHour.getString("icon"),
                            jsonHour.getString("condition"),
                            jsonHour.getLong("hour_ts"));
                }

                //считываю данные частей дня
                JSONObject parts = jsonWeatherDay.getJSONObject("parts");
                Iterator<String> keys = parts.keys();

                PartDay[] partsDay = new PartDay[Weather.LENGTH_PART_DAY];

                int j = 0; // индекс partsDay
                while (keys.hasNext()){
                    String key = keys.next();

                    if(!setPartDay.contains(key)) continue;

                    if (parts.get(key) instanceof JSONObject){
                        JSONObject jsonPart = parts.getJSONObject(key);

                        partsDay[j] = new PartDay(jsonPart.getInt("pressure_mm"),
                                jsonPart.getInt("humidity"), key,
                                jsonPart .getInt("temp_avg"),
                                jsonPart.getString("icon"),
                                jsonPart.getString("condition"));

                        j++;
                    }
                }

                weathers.add(new Weather(temp, feels_like, icon, condition,
                        hourlyWeathers, date, partsDay));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weathers;
    }
}
