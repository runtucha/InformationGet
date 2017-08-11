package com.example.informationget.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by glenn_cui on 17-8-11.
 */
public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
