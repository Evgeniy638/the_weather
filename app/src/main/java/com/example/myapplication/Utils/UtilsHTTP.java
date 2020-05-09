package com.example.myapplication.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.pixplicity.sharp.Sharp;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UtilsHTTP {
    private static final String url = "https://yastatic.net/weather/i/icons/blueye/color/svg/";
    private static OkHttpClient httpClient;

    public static void fetchSvg(Context context, String icon, final ImageView imageView) {
        if(httpClient == null){
            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1024))
                    .build();
        }

        Request request = new Request.Builder().url(url + icon + ".svg").build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream stream = response.body().byteStream();
                Sharp.loadInputStream(stream).into(imageView);
                stream.close();
            }
        });
    }
}
