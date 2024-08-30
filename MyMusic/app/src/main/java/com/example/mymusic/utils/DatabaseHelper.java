package com.example.mymusic.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mymusic.R;
import com.example.mymusic.data.WebInfo;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database.db";
    public static final int DATABASE_VERSION = 1;
    public static final int DATABASE_NEW_VERSION = 2;
    public static final String TABLE_NAME_WEBURLS ="weburls";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WEBURL = "weburl";
    public static final String COLUMN_WEBTITLE = "webtitle";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ICON = "icon";

    private Context mContext;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE " + TABLE_NAME_WEBURLS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WEBURL + " TEXT,"
                + COLUMN_WEBTITLE + " TEXT,"
                + COLUMN_NUMBER + " INTEGER,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_ICON + " INTEGER"
                + ")";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //按照网页信息对象插入一条网页信息
    public void insertOneWebUrl(WebInfo webInfo){


    }

    //插入一条网页信息
    public void inserOneWebUrlByUrl(String webUrl , String title){
        Cursor cursor = getOneWebUrlByWebUrl(webUrl);  //查询网页地址是否已存在数据库
        if (cursor!= null && cursor.moveToFirst()) {
            // 表示有数据
            Toast.makeText(mContext,"网页地址已存在："+webUrl,Toast.LENGTH_SHORT).show();

        } else {
            // 表示没有数据,插入网页信息
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            int number = getTotalAmount()+1;
            values.put(COLUMN_WEBURL,webUrl);
            values.put(COLUMN_WEBTITLE,title);
            values.put(COLUMN_NUMBER,number);
            values.put(COLUMN_ICON,R.drawable.bm_default);
            db.insert(TABLE_NAME_WEBURLS,null,values);
            db.close();
        }

        cursor.close();
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
    public Cursor getOneWebUrlByWebUrl(String webUrl){

        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select * from "+TABLE_NAME_WEBURLS +" where " +COLUMN_WEBURL + " = '" + webUrl +"'";
        Log.d("TAG", "QuerySql: "+QuerySql);
        Cursor cursor = db.rawQuery(QuerySql, null);

        return cursor;
    }

    //查询已保存书签数量，用于保存排序编号
    public int getTotalAmount(){
        int dataAmount = 0;
        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select count(*) from "+TABLE_NAME_WEBURLS ;
        Cursor cursor = db.rawQuery(QuerySql, null);
//        cursor.moveToFirst();
        if(cursor.moveToFirst()){
            dataAmount = cursor.getInt(0);
        }
        cursor.close();
        return dataAmount;
    }

    //查询所有已保存书签
    public Cursor getAllBookmarks(){

        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select * from "+TABLE_NAME_WEBURLS ;
        Log.d("TAG", "QuerySql: "+QuerySql);
        Cursor cursor = db.rawQuery(QuerySql, null);

        return cursor;

    }


}
