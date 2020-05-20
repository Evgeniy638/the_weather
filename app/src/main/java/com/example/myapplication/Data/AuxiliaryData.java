package com.example.myapplication.Data;

import java.text.DateFormatSymbols;
import java.util.HashMap;

public class AuxiliaryData {
    private static DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(){
        @Override
        public String[] getMonths() {
            return new String[]{
                    "января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"
            };
        }
    };

    private static HashMap<String,String> mapConditions = new HashMap<>();
    static {
        mapConditions.put("clear", "ясно");
        mapConditions.put("partly-cloudy", "малооблачно");
        mapConditions.put("cloudy", "облачно с прояснениями");
        mapConditions.put("overcast", "пасмурно");
        mapConditions.put("partly-cloudy-and-light-rain", "небольшой дождь");

        mapConditions.put("partly-cloudy-and-rain", "дождь");
        mapConditions.put("overcast-and-rain", "сильный дождь");
        mapConditions.put("overcast-thunderstorms-with-rain", "сильный дождь, гроза");
        mapConditions.put("cloudy-and-light-rain", "небольшой дождь");
        mapConditions.put("overcast-and-light-rain", "небольшой дождь");

        mapConditions.put("cloudy-and-rain", "дождь");
        mapConditions.put("overcast-and-wet-snow", "дождь со снегом");
        mapConditions.put("partly-cloudy-and-light-snow", "небольшой снег");
        mapConditions.put("partly-cloudy-and-snow", "снег");
        mapConditions.put("overcast-and-snow", "снегопад");

        mapConditions.put("cloudy-and-light-snow", "небольшой снег");
        mapConditions.put("overcast-and-light-snow", "небольшой снег");
        mapConditions.put("cloudy-and-snow", "снег");
    }

    public static DateFormatSymbols getDateFormatSymbols() {
        return dateFormatSymbols;
    }

    public static String getMapConditions(String key) {
        return mapConditions.get(key);
    }
}
