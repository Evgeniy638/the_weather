package com.example.myapplication;

public class Weather {
    private double temperature;
    private double feels_like;
    private double pressure;
    private double humidity;
    private double clouds;
    private Wind wind;

    public Weather(double temperature, double feels_like,
                   double pressure, double humidity, double clouds,
                   double windDeg, double windSpeed) {
        this.temperature = temperature;
        this.feels_like = feels_like;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        wind = new Wind(windSpeed, windDeg);
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getClouds() {
        return clouds;
    }


    public Wind getWind() {
        return wind;
    }

    private class Wind{
        private double speed;
        private double deg;

        Wind(double speed, double deg){

        }

        public double getSpeed() {
            return speed;
        }

        public double getDeg() {
            return deg;
        }
    }
}
