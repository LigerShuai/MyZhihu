package com.liger.myzhihu.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liger on 2016/6/3 19:46.
 */
public class OkHttpUtil {
    /* 全局使用一个 OkHttpClient */
    private static final OkHttpClient client = new OkHttpClient();
    static {
    }

    public static Response execute(Request request)throws IOException {
        Response response = client.newCall(request).execute();
        return response;
    }

    /**
     * 开启异步线程访问网络
     */
    public static void enqueue(Request request, Callback responseCallback) {
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     */
    public static void enqueue(Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("shuai", "test");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("shuai", "test");
            }
        });
    }

    public static String getStringFromServer(String url)throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private static final String CHARSET_NAME = "utf-8";


}
