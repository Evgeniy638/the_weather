package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.example.myapplication.Data.Weather;
import com.example.myapplication.Utils.URLReaderWeather;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_CODE_LOCATION = 1;
    private LocationManager locationManager;

    private Handler handler;

    private static MyWeatherAdapter myWeatherAdapter;
    public static final ArrayList<Weather> weathers = new ArrayList<>();
    private ListView listView;

    private static boolean isInit = false;

    public static final String APP_PREFERENCES = "data_weathers";
    public static final String APP_PREFERENCES_NAME = "weathers";

    private SharedPreferences sharedPreferences;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        //получаю SharedPreferences, где хранятся кешированные данные
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                weathers.addAll((ArrayList<Weather>)msg.obj);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(APP_PREFERENCES_NAME, Weather.toStringIntoArrayList(weathers));
                editor.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            startAdapter();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        if(isInit && !hasAccessFineLocation() && isOnline()){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);
        }else if(!isInit && (!isOnline() || !hasAccessFineLocation())) {
            weathers.addAll(Weather.intoStringToArrayList(
                    sharedPreferences.getString(APP_PREFERENCES_NAME, "")));

            startAdapter();
        }else if (!isInit){
            requestLocationUpdates();
        }

        if(isInit) {
            listView.setAdapter(myWeatherAdapter);
        }

        isInit = true;
    }

    private void startAdapter(){
        if (myWeatherAdapter == null) {
            myWeatherAdapter = new MyWeatherAdapter(MainActivity.this, weathers);
        }

        listView.setAdapter(myWeatherAdapter);

        locationManager.removeUpdates(locationListener);
    }

    public static MyWeatherAdapter getMyWeatherAdapter(){
        return myWeatherAdapter;
    }

    private boolean hasAccessFineLocation(){
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private boolean hasPermission(String perm){
        return PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, perm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_LOCATION && hasAccessFineLocation())
            requestLocationUpdates();
        else if (requestCode == REQUEST_CODE_LOCATION)
            finish();
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates(){
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0, 1000, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            URLReaderWeather urlReaderWeather = new URLReaderWeather(handler,
                    Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            urlReaderWeather.start();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            weathers.addAll(Weather.intoStringToArrayList(
                    sharedPreferences.getString(APP_PREFERENCES_NAME, "")));

            startAdapter();
        }
    };

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}
