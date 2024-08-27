package com.example.mymusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mymusic.data.WebInfo;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_WEBURLS ="weburls";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WEBURL = "weburl";
    public static final String COLUMN_WEBTITLE = "webtitle";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ICON = "icon";


    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME_WEBURLS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WEBURL + " TEXT,"
                + COLUMN_WEBTITLE + " TEXT,"
                + COLUMN_NUMBER + " INTEGER,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_ICON + " TEXT"
                + ")";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //插入一条网页信息
    public void insertOneWebUrl(WebInfo webInfo){


    }

    //通过网页标题模糊查询
    public Cursor getAllWebUrlsByWebTitles(String webTitle){
        Cursor cursor =null;



        return cursor;
    }

    //通过网页地址模糊查询
    public Cursor getAllWebUrlsByWebUrls(String webUrl){
        Cursor cursor =null;



        return cursor;
    }

    //通过网页地址精确查询
    public Cursor getOneWebUrlsByWebUrl(String webUrl){
        Cursor cursor =null;



        return cursor;
    }


}
