package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<Weather> weathers = new ArrayList<>();

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                final ArrayList<Weather> arrayList = (ArrayList<Weather>) msg.obj;

                for(Weather weather: arrayList){
                    weathers.add(weather);
                }
            }
        };

        URLConnectionReader urlConnectionReader = new URLConnectionReader(handler,
                "Moscow", "ru");
        urlConnectionReader.start();

        TextView textView = findViewById(R.id.text123);
    }
}
