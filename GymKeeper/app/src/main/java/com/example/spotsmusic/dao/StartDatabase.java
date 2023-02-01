package com.example.spotsmusic.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StartDatabase extends SQLiteOpenHelper {
    private static  final String CREATE_Start = "create table Start(id integer primary key autoincrement,Title text,Tab text, Time integer,GroupTime integer,Ca integer,Type text)";
    private Context mContext;
    public StartDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(CREATE_Start);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //升级数据库
        sqLiteDatabase.execSQL("drop table if exists Start");
        onCreate(sqLiteDatabase);
    }
}
