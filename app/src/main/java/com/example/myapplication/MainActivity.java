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
                weathers.addAll((ArrayList<Weather>)msg.obj);
            }
        };

        URLConnectionReader urlConnectionReader = new URLConnectionReader(handler,
                "55.75396", "37.620393");
        urlConnectionReader.start();

        TextView textView = findViewById(R.id.text123);
    }
}
