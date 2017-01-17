package com.example.c1103304.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by c1103304 on 2017/1/6.
 */

public class msqlite {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "toilets.db";
    private static final String TABLE_NAME = "table";
    public static final String CATEGORY_COLUMN_ID = "_id";
    public static final String CATEGORY_COLUMN_NAME = "name";
    public static final String CATEGORY_LAT = "lat";
    public static final String CATEGORY_LON = "lon";
    public static final String CATEGORY_ADDRESS = "address";
    public static final String CATEGORY_COUNTRY = "country";
    Category openHelper;
    private SQLiteDatabase database;

    public msqlite(Context context){
        openHelper = new Category(context);
        database = openHelper.getWritableDatabase();
    }
    public void saveCategoryRecord(String id, String name,String lat,String lon,String address,String country) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_ID, id);
        contentValues.put(CATEGORY_COLUMN_NAME, name);
        contentValues.put(CATEGORY_ADDRESS, address);
        contentValues.put(CATEGORY_LAT, lat);
        contentValues.put(CATEGORY_LON, lon);
        contentValues.put(CATEGORY_COUNTRY, country);
        database.insert(TABLE_NAME, null, contentValues);
        Log.d("MYLOG","INSERT ONE DATA: sss");
    }
    public void saveCategoryRecord(String name,String lat,String lon,String address,String country) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_NAME, name);
        contentValues.put(CATEGORY_ADDRESS, address);
        contentValues.put(CATEGORY_LAT, lat);
        contentValues.put(CATEGORY_LON, lon);
        contentValues.put(CATEGORY_COUNTRY, country);
        database.insert(TABLE_NAME, null, contentValues);
        Log.d("MYLOG","INSERT ONE DATA: sss");
    }
    public Cursor getTimeRecordList() {
        return database.rawQuery("select * from " + TABLE_NAME, null);
    }
    private class Category extends SQLiteOpenHelper {

        public Category(Context context) {
            // TODO Auto-generated constructor stub
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                    + CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY, " + CATEGORY_COLUMN_NAME +
                    " TEXT, " + CATEGORY_ADDRESS + " TEXT, " +
                    CATEGORY_LAT + " TEXT, " + CATEGORY_LON + " TEXT, " + CATEGORY_COUNTRY + " )" );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
            onCreate(db);
        }

    }
}
