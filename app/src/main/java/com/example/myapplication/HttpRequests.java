package com.example.myapplication;

import android.graphics.drawable.VectorDrawable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HttpRequests {
    @GET("/weather/i/icons/blueye/color/svg/{icon}.svg")
    Call<ResponseBody> getIconWeather(@Path("icon") String icon);
}
