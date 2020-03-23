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

public class URLConnectionReader extends Thread{
    private final static String key = "&appid=794fd93fd7e0c10ee376d9e89b082808";
    private final static String baseURL = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private final static String lang = "&lang=ru";

    private String city;
    private String country;
    private Handler handler;

    URLConnectionReader(Handler handler, String city, String country){
        this.city = city;
        this.country = country;
        this.handler = handler;
    }

    @Override
    public void run() {
        ArrayList<Weather> weathers = getWeather(city, country);

        Message message = new Message();
        message.obj = weathers;

        handler.handleMessage(message);
    }

    public ArrayList<Weather> getWeather(String city, String country){
        ArrayList<Weather> weathers = new ArrayList<>();

        try {
            URL openWeather = new URL(baseURL + city + "," + country + key + lang);
            URLConnection urlConnection = openWeather.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            String jsonLine = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                jsonLine += inputLine;
            in.close();

            JSONObject jsonObject = new JSONObject(jsonLine);
            JSONArray list = (JSONArray)jsonObject.get("list");

            for (int i = 0; i < list.length(); i++) {
                JSONObject item = (JSONObject)list.get(i);

                JSONObject main = (JSONObject)item.get("main");
                JSONObject clouds = (JSONObject) item.get("clouds");
                JSONObject wind = (JSONObject) item.get("wind");

                weathers.add(new Weather(main.getDouble("temp"), main.getDouble("feels_like"),
                        main.getDouble("pressure"), main.getDouble("humidity"),
                        clouds.getDouble("all"), wind.getDouble("speed"),
                        wind.getDouble("deg")));
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
