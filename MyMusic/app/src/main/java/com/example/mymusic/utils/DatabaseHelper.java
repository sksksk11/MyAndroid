package com.example.mymusic.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mymusic.R;
import com.example.mymusic.data.Keyword;
import com.example.mymusic.data.WebHistory;
import com.example.mymusic.data.WebInfo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database.db";
    public static final int DATABASE_VERSION = 1;
    public static final int DATABASE_NEW_VERSION = 2;
    public static final String TABLE_NAME_WEBURLS = "weburls";    //书签表
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WEBURL = "weburl";
    public static final String COLUMN_WEBTITLE = "webtitle";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_ISDEL = "isdel";

    public static final String CATEGORY_DEFAULT = "未分类";

    public static final String TABLE_NAME_HISTORY = "browsehistory";    //浏览历史表
    public static final String COLUMN_ICONURL = "iconurl";
    public static final String COLUMN_VISITTIME = "visittime";
    public static final int MAX_HISTORY_NUM = 300;   //最大保存历史浏览记录数


    public static final String TABLE_NAME_KEYWORD = "keyword";    //关键词表
    public static final String COLUMN_KEYWORD = "keyword";
    public static final String COLUMN_CLICKTIMES = "clicktimes";


    private Context mContext;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public DatabaseHelper(Context context) {
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
                + COLUMN_ICON + " INTEGER,"
                + COLUMN_ISDEL + " TEXT"
                + ")";
        db.execSQL(createTableQuery);

        String createHistoryTableQuery = "CREATE TABLE " + TABLE_NAME_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WEBURL + " TEXT,"
                + COLUMN_WEBTITLE + " TEXT,"
                + COLUMN_ICON + " TEXT,"     //网页 favicon.icon 图标地址
                + COLUMN_VISITTIME + " TEXT"     //访问时间
                + ")";
        db.execSQL(createHistoryTableQuery);

        String createKeywordTableQuery = "CREATE TABLE " + TABLE_NAME_KEYWORD + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_KEYWORD + " TEXT,"
                + COLUMN_CLICKTIMES + " TEXT,"
                + COLUMN_ICON + " TEXT"     // 关键词 图标地址
                + ")";
        db.execSQL(createKeywordTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            String createHistoryTableQuery = "CREATE TABLE " + TABLE_NAME_HISTORY + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_WEBURL + " TEXT,"
                    + COLUMN_WEBTITLE + " TEXT,"
                    + COLUMN_ICON + " TEXT,"     //网页 favicon.icon 图标地址
                    + COLUMN_VISITTIME + " TEXT"     //访问时间
                    + ")";
            db.execSQL(createHistoryTableQuery);


            String createKeywordTableQuery = "CREATE TABLE " + TABLE_NAME_KEYWORD + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_KEYWORD + " TEXT,"
                    + COLUMN_CLICKTIMES + " TEXT,"
                    + COLUMN_ICON + " TEXT"     // 关键词 图标地址
                    + ")";
            db.execSQL(createKeywordTableQuery);

            //修改表结构
//            String addColumnQuery = "ALTER TABLE "+ TABLE_NAME_HISTORY +" ADD COLUMN " + COLUMN_VISITTIME + " TEXT";
//            db.execSQL(addColumnQuery);

        }

    }

    //按照网页信息对象插入一条网页信息
    public void insertOneWebUrl(WebInfo webInfo) {


    }

    //插入一条网页信息
    public void inserOneWebUrlByUrl(String webUrl, String title) {
        Cursor cursor = getOneWebUrlByWebUrl(webUrl);  //查询网页地址是否已存在数据库
        if (cursor != null && cursor.moveToFirst()) {
            // 表示有数据
            Toast.makeText(mContext, "网页地址已存在：" + webUrl, Toast.LENGTH_SHORT).show();

        } else {
            // 表示没有数据,插入网页信息
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            int number = getTotalAmount() + 1;
            values.put(COLUMN_WEBURL, webUrl);
            values.put(COLUMN_WEBTITLE, title);
            values.put(COLUMN_NUMBER, number);
            values.put(COLUMN_ICON, R.drawable.bm_default);
            values.put(COLUMN_ISDEL, "N");
            values.put(COLUMN_CATEGORY, CATEGORY_DEFAULT);
//            values.put(COLUMN_CATEGORY,"分类55");
            db.insert(TABLE_NAME_WEBURLS, null, values);
            db.close();
        }

        cursor.close();
    }

    //通过网页标题模糊查询
    public Cursor getAllWebUrlsByWebTitles(String webTitle) {
        Cursor cursor = null;


        return cursor;
    }

    //通过网页地址模糊查询
    public Cursor getAllWebUrlsByWebUrls(String webUrl) {
        Cursor cursor = null;


        return cursor;
    }

    //通过网页地址精确查询
    public Cursor getOneWebUrlByWebUrl(String webUrl) {

        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select * from " + TABLE_NAME_WEBURLS + " where " + COLUMN_WEBURL + " = '" + webUrl + "' AND " + COLUMN_ISDEL + " = 'N'";
        Log.d("TAG", "QuerySql: " + QuerySql);
        Cursor cursor = db.rawQuery(QuerySql, null);

        return cursor;
    }

    //查询已保存书签数量，用于保存排序编号
    public int getTotalAmount() {
        int dataAmount = 0;
        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select count(*) from " + TABLE_NAME_WEBURLS + " where " + COLUMN_ISDEL + " = 'N'";
        Cursor cursor = db.rawQuery(QuerySql, null);
//        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            dataAmount = cursor.getInt(0);
        }
        cursor.close();
        return dataAmount;
    }

    //查询所有已保存书签
    public Cursor getAllBookmarks() {

        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select * from " + TABLE_NAME_WEBURLS + " where " + COLUMN_ISDEL + " = 'N' ORDER BY " + COLUMN_NUMBER;
        Log.d("TAG", "QuerySql: " + QuerySql);
        Cursor cursor = db.rawQuery(QuerySql, null);

        return cursor;

    }

    //按ID更新标题,返回更新数据量,newIconid 传入0 使用默认图标
    public int updataTitleById(int id, String newTitle, String newCategory, int newIconid) {

        String stringId = id + "";
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WEBTITLE, newTitle);
        if (newCategory == null) {
            values.put(COLUMN_CATEGORY, CATEGORY_DEFAULT);
        } else {
            values.put(COLUMN_CATEGORY, newCategory);
        }

        //newIconid 传入0时， 使用默认图标
        if (newIconid == 0) {
            newIconid = R.drawable.bm_default;
        }

        values.put(COLUMN_ICON, newIconid);

        int updatedRows = db.update(TABLE_NAME_WEBURLS, values, COLUMN_ID + " = ?", new String[]{stringId});
        return updatedRows;

    }


    //按ID标记需要删除数据
    public int deleteBookmarkById(int id) {
        String stringId = id + "";
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ISDEL, "Y");
        int deleteRows = db.update(TABLE_NAME_WEBURLS, values, COLUMN_ID + " = ?", new String[]{stringId});
        return deleteRows;
    }

    //按当前列表条目数量，重新更新排序，在删除条目后更新排序序号用
    //传入当前列表，按条目数量更新排序序号
    public int updateBookmarkListOrderNum(List<WebInfo> bookmarkList) {
        int updateRows = 0;
        //传入列表不为空时才更新序号
        if (bookmarkList != null) {
            SQLiteDatabase db = getWritableDatabase();
            for (int i = 0; i < bookmarkList.size(); i++) {
//                Log.d("TAG", "title: "+bookmarkList.get(i).getWebTitle()+" ,id:"+bookmarkList.get(i).getId());
                ContentValues values = new ContentValues();
                values.put(COLUMN_NUMBER, i + 1);
                String stringId = bookmarkList.get(i).getId() + "";
                db.update(TABLE_NAME_WEBURLS, values, COLUMN_ID + " = ? AND " + COLUMN_ISDEL + " = 'N'", new String[]{stringId});

            }

            updateRows = bookmarkList.size();
        }

        return updateRows;
    }


    //读取数据库，获取当前所有分类,加入到列表
    public List<String> getAllCategory() {
        List<String> categoryList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        //按分类数量倒序
//        String QuerySql = "SELECT " + COLUMN_CATEGORY + " ,  COUNT(*) AS COUNT FROM "+TABLE_NAME_WEBURLS + " WHERE " + COLUMN_ISDEL + " = 'N' GROUP BY " + COLUMN_CATEGORY +" ORDER BY COUNT DESC ";
        String QuerySql = "SELECT " + COLUMN_CATEGORY + " ,  COUNT(*) AS COUNT FROM " + TABLE_NAME_WEBURLS + " GROUP BY " + COLUMN_CATEGORY + " ORDER BY COUNT DESC ";
        Log.d("TAG", "QuerySql: " + QuerySql);
        Cursor cursor = db.rawQuery(QuerySql, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
                Log.d("TAG", "category: " + category);
                if (category != null && !category.trim().isEmpty()) {
                    categoryList.add(category.trim());
                }

            } while (cursor.moveToNext());

        }
        cursor.close();
        if (categoryList == null) {
            categoryList.add(CATEGORY_DEFAULT);
        }

        return categoryList;
    }

    /*************

     历史记录相关方法
     **************/

    //保存浏览历史
    public void saveToHistory(String url, String title, @Nullable String iconurl) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_WEBURL, url);
        values.put(COLUMN_WEBTITLE, title);
        if (iconurl == null) {
            iconurl = "https://mat1.gtimg.com/qqcdn/qqindex2021/favicon.ico";
        }
        values.put(COLUMN_ICON, iconurl);
        values.put(COLUMN_VISITTIME, timeTools.getCurrentTimeFormatted());

        db.insert(TABLE_NAME_HISTORY, null, values);
        db.close();

        //当超出最大保存记录数时，删除最早的纪录
        int currentRows = getTotalHistoryRecords();
        if (currentRows >= MAX_HISTORY_NUM) {
            int deletRows = currentRows - MAX_HISTORY_NUM + 1;
            Log.d("TAG", "超出最大记录数: " + deletRows + ",最大记录设置：" + MAX_HISTORY_NUM);
            deleteHistoryRecords(deletRows);
        }


    }

    //查询浏览历史条数
    public int getTotalHistoryRecords() {
        int dataAmount = 0;
        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select count(*) from " + TABLE_NAME_HISTORY;
        Cursor cursor = db.rawQuery(QuerySql, null);
//        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            dataAmount = cursor.getInt(0);
        }
        cursor.close();
        return dataAmount;
    }


    //按参数删除历史浏览记录数
    public void deleteHistoryRecords(int deleteRows) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_HISTORY + " WHERE id IN (SELECT id FROM " + TABLE_NAME_HISTORY + " ORDER BY id ASC LIMIT " + deleteRows + ")";
        db.execSQL(query);
        db.close();
    }

    //查询所有历史记录
    public List<WebHistory> getAllWebHistoryList() {
        List<WebHistory> mWebHistoryList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select * from " + TABLE_NAME_HISTORY + " ORDER BY " + COLUMN_VISITTIME + " DESC";
        Log.d("TAG", "QuerySql: " + QuerySql);
        Cursor cursor = db.rawQuery(QuerySql, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(COLUMN_WEBURL));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_WEBTITLE));
                @SuppressLint("Range") String icon = cursor.getString(cursor.getColumnIndex(COLUMN_ICON));
                @SuppressLint("Range") String visittime = cursor.getString(cursor.getColumnIndex(COLUMN_VISITTIME));
                Log.d("TAG", "id: " + id + " , title: " + title + " ,url:" + url + " ,icon:" + icon + " ,visittime:" + visittime);
                WebHistory mwebhistory = new WebHistory(id,url,title,icon,visittime);
                mWebHistoryList.add(mwebhistory);

            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return mWebHistoryList;
    }

    //按关键字模糊查询浏览历史记录
    public List<WebHistory> getHistoryByKeyword(String searchString){
        List<WebHistory> mWebHistoryList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select * from " + TABLE_NAME_HISTORY + " WHERE " + COLUMN_WEBTITLE +" LIKE '%" +searchString+ "%' OR "+COLUMN_WEBURL+" LIKE '%"+searchString+ "%' ORDER BY " + COLUMN_VISITTIME + " DESC";
        Log.d("TAG", "QuerySql: " + QuerySql);
        Cursor cursor = db.rawQuery(QuerySql, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(COLUMN_WEBURL));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_WEBTITLE));
                @SuppressLint("Range") String icon = cursor.getString(cursor.getColumnIndex(COLUMN_ICON));
                @SuppressLint("Range") String visittime = cursor.getString(cursor.getColumnIndex(COLUMN_VISITTIME));
                Log.d("TAG", "id: " + id + " , title: " + title + " ,url:" + url + " ,icon:" + icon + " ,visittime:" + visittime);
                WebHistory mwebhistory = new WebHistory(id,url,title,icon,visittime);
                mWebHistoryList.add(mwebhistory);

            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return mWebHistoryList;

    }

    /*************

     关键词相关方法
     **************/

    //查询所有关键词，传入参数，按id/首字符/点击次数/ 排序
    public List<Keyword> getAllKeywordList(int ordbyStr ,@Nullable boolean isDESC ){
        List<Keyword> keywordList = new ArrayList<>();
        String order = "";

        switch (ordbyStr){
            case 1:
                order = COLUMN_ID;
                break;
            case 2:
                order = COLUMN_KEYWORD;
            case 3:
                order = COLUMN_CLICKTIMES;

            default:order = COLUMN_KEYWORD;
        }
        String orderDesc = "";
        if(isDESC){ orderDesc = "DESC"; }

        SQLiteDatabase db = getReadableDatabase();
        String QuerySql = "select * from " + TABLE_NAME_KEYWORD + " ORDER BY " + order + " " + orderDesc;
        Log.d("TAG", "QuerySql: " + QuerySql);
        Cursor cursor = db.rawQuery(QuerySql, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String keyword = cursor.getString(cursor.getColumnIndex(COLUMN_KEYWORD));
                @SuppressLint("Range") int clicktimes = cursor.getInt(cursor.getColumnIndex(COLUMN_CLICKTIMES));
                @SuppressLint("Range") int  icon = cursor.getInt(cursor.getColumnIndex(COLUMN_ICON));

                Log.d("TAG", "id: " + id + " , keyword: " + keyword + " ,clicktimes:" + clicktimes + " ,icon:" + icon );
                Keyword mkeyword = new Keyword(id,keyword,clicktimes,icon);
                keywordList.add(mkeyword);

            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }


        return keywordList;
    }


    //精确查询关键词是否已存在
    public boolean isKeywordHasAlreadyExists(String inputStr){
        if(inputStr !=null && !inputStr.trim().isEmpty()){
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_KEYWORD + " WHERE "+ COLUMN_KEYWORD +" = '"+ inputStr.trim() +"'";

            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){

                return true;
            }

            cursor.close();
            db.close();
        }


        return false;
    }


    //插入1个关键词到数据库，成功插入返回1，否则返回0；
    public int insertKeywordIntoDataBase(Keyword keyword){
        int rows = 0;
        if (keyword == null) {
            return rows;
        }

        //查询是否已存在，不存在插入
        if(!isKeywordHasAlreadyExists(keyword.getKeyword())){

            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(COLUMN_KEYWORD, keyword.getKeyword().toString());
            values.put(COLUMN_CLICKTIMES, keyword.getClicktimes());

            int icon =  R.drawable.logo_strava;
            if(keyword.getIcon() != null ){  icon = keyword.getIcon(); }
            values.put(COLUMN_ICON,icon);

            db.insert(TABLE_NAME_KEYWORD, null, values);
            rows =1;
            db.close();

        }else {
            Toast.makeText(mContext,"关键词已存在："+keyword.getKeyword().toString(),Toast.LENGTH_SHORT).show();
        }

        return rows;

    }



    //修改关键词


    //按id更新关键词点击次数
    public void updateClickTimesById(Integer id,Integer clickTimes){
        if (id != null) {
            String stringId = id + "";

            if (clickTimes != null) {
                clickTimes++;
            }else {
                clickTimes = 0;
            }

            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CLICKTIMES, clickTimes);
            db.update(TABLE_NAME_KEYWORD, values, COLUMN_ID + " = ?", new String[]{stringId});

            db.close();
        }

    }



}