package com.example.zhihuribao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    public MyDataBaseHelper(@Nullable Context context) {
        //下面这个"database"是数据库的名字,version是版本号
        super(context, "database", null, 28);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //下面这句是简单的SQL语句,创建数据库的表
        String TABLE_USER = "create table user(id_user INTEGER PRIMARY KEY autoincrement,number text,name text,password text,picture blob);";
        sqLiteDatabase.execSQL(TABLE_USER);
        String TABLE_COLLECT = "create table collect(id_collect INTEGER PRIMARY KEY autoincrement,id_user text,id_news text,if_collect text,title_news text,url_news text);";
        sqLiteDatabase.execSQL(TABLE_COLLECT);
        String TABLE_ZAN = "create table zan(id_zan INTEGER PRIMARY KEY autoincrement,id_user text,id_news text,if_zan text,title_news text,url_news text);";
        sqLiteDatabase.execSQL(TABLE_ZAN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //更新数据库版本时才会执行onUpgrade
        sqLiteDatabase.execSQL("drop table if exists user");
        sqLiteDatabase.execSQL("drop table if exists collect");
        sqLiteDatabase.execSQL("drop table if exists zan");
        onCreate(sqLiteDatabase);
    }
}
