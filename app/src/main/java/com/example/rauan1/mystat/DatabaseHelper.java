package com.example.rauan1.mystat;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "restrictions.db";
    public static final String DATABASE_TABLE = "restrictions";
    public static final String ID = "_id";
    public static final String NAME = "_name";
    public static final String RESTRICTIONS_IN_MIN = "_restrictions";
    public static final String DAY_WEEK = "_day";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE + "(" + ID + " integer primary key autoincrement, "+NAME+" text not null, "+RESTRICTIONS_IN_MIN + " integer not null, "+DAY_WEEK+" text not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+DATABASE_TABLE);
        onCreate(db);
    }
    public void insertNote(String pkgnameofapp,String day,int minutes){
        SQLiteDatabase db = this.getWritableDatabase();
        if (!CheckIsDataAlreadyInDBorNot(pkgnameofapp,day)){
        ContentValues cv = new ContentValues();
        cv.put(NAME,pkgnameofapp);
        cv.put(RESTRICTIONS_IN_MIN,minutes);
        cv.put(DAY_WEEK,day);
        db.insert(DATABASE_TABLE,null,cv);}
        else{
            String strSQL = "UPDATE "+DATABASE_TABLE+" SET "+RESTRICTIONS_IN_MIN+" = "+minutes+" WHERE "+NAME+" = "+ pkgnameofapp+" AND "+DAY_WEEK+" = " + day;
            db.execSQL(strSQL);
        }

    }

    public ArrayList<rest> getReceives() {
        ArrayList<rest> places = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String get_places = "select * from "+DATABASE_TABLE;
        Cursor c = db.rawQuery(get_places,null);
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            rest place = new rest();
            place.name = c.getString(c.getColumnIndex(NAME));
            place.day = c.getString(c.getColumnIndex(DAY_WEEK));
            place.minutes = c.getInt(c.getColumnIndex(RESTRICTIONS_IN_MIN));
            places.add(place);
        }
        return places;
    }
    public boolean CheckIsDataAlreadyInDBorNot(String pkgnameofapp, String day) {
        SQLiteDatabase sqldb = getReadableDatabase();
        String Query = "Select * from " + DATABASE_TABLE + " where " + NAME + " = '" + pkgnameofapp +"' AND "+ DAY_WEEK + " = '" + day+"'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}