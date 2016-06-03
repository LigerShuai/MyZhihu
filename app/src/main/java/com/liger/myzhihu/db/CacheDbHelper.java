package com.liger.myzhihu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shuai on 2015/12/18.
 */
public class CacheDbHelper extends SQLiteOpenHelper {

   /* private static final String CACHElIST = "create table if not exists CacheList(" +
            "id INTEGER primary key autoincrement," +
            "date INTEGER unique," +
            "json text)";*/
    private static final String CACHELIST = "create table if not exists CacheList (id INTEGER primary key autoincrement,date INTEGER unique,json text)";

    public CacheDbHelper(Context context, int version) {
        super(context, "cache.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CACHELIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
