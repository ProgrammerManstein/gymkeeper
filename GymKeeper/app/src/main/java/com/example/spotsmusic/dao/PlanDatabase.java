package com.example.spotsmusic.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlanDatabase extends SQLiteOpenHelper {
    private static  final String CREATE_Plan = "create table Plan(id integer primary key autoincrement, Time integer, " +
            "Content text,Type text)";
    private Context mContext;
    public PlanDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(CREATE_Plan);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //升级数据库
        sqLiteDatabase.execSQL("drop table if exists Plan");
        onCreate(sqLiteDatabase);
    }
}
