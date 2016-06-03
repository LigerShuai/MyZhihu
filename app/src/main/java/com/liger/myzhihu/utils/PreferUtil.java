package com.liger.myzhihu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Shuai on 2015/12/18.
 */
public class PreferUtil {

    public static void putStringToDefault(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(key, value).commit();
    }

    public static String getStringFromDefault(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        String str = preferences.getString(key, value);
        return preferences.getString(key,value);
    }
}
