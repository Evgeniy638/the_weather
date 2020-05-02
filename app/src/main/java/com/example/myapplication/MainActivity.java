package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final ArrayList<Weather> weathers = new ArrayList<>();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor1));
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        listView = findViewById(R.id.listView);

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                weathers.addAll((ArrayList<Weather>)msg.obj);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyWeatherAdapter myWeatherAdapter = new MyWeatherAdapter(getApplicationContext(), weathers);
                            listView.setAdapter(myWeatherAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        URLReaderWeather urlReaderWeather = new URLReaderWeather(handler,
                "55.75396", "37.620393");
        urlReaderWeather.start();
    }
}
