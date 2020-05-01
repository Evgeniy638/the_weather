package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

import android.icu.util.Measure;
import android.os.Handler;
import android.os.Message;

public class URLReaderWeather extends Thread{
    private final static String key = "d2e19a3a-eb4d-42a3-8e04-b76e7522c141";
    private final static String baseURL = "https://api.weather.yandex.ru/v1/forecast?limit=7&extra=true&lang=ru_RU";

    private String lat = "&lat=";
    private String lon = "&lon=";
    private Handler handler;

    private final String X_Yandex_API_Key = "X-Yandex-API-Key";

    URLReaderWeather(Handler handler, String lat, String lon){
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

                JSONArray hours = jsonWeatherDay.getJSONArray("hours");

                HourlyWeather[] hourlyWeathers = new HourlyWeather[hours.length()];

                for (int hour = 0; hour < hours.length(); hour++) {
                    JSONObject jsonHour = hours.getJSONObject(hour);

                    hourlyWeathers[hour] = new HourlyWeather(jsonHour.getInt("temp"),
                            jsonHour.getString("icon"),
                            jsonHour.getString("condition"));
                }

                weathers.add(new Weather(temp, feels_like, icon, condition, hourlyWeathers));
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
