package com.liger.myzhihu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 封装 async-http 请求的类
 * Created by Shuai on 2015/12/17.
 */
public class HttpUtils {

    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void get(String url, ResponseHandlerInterface responseHandlerInterface) {
        asyncHttpClient.get(Constants.BASEURL + url, responseHandlerInterface);
    }

    public static void getImage(String url, ResponseHandlerInterface responseHandlerInterface) {
        asyncHttpClient.get(url, responseHandlerInterface);
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
